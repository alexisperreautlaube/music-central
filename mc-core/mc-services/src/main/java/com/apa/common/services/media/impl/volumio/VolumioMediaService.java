package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.repositories.MediaDistanceRepository;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.media.MediaService;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class VolumioMediaService extends AbstractMediaService<VolumioMedia> implements MediaService<VolumioMedia> {

    private final VolumioMediaRepository volumioMediaRepository;

    private final TidalMediaService tidalMediaService;

    private final MediaDistanceRepository mediaDistanceRepository;

    private final PlexMediaService plexMediaService;

    @Override
    @Transactional
    public VolumioMedia save(VolumioMedia volumioMedia) {
        return volumioMediaRepository.save(volumioMedia);
    }

    @Override
    public List<VolumioMedia> findPerfectMatch(String artistName, String albumName, String trackTitle, String trackIndex) {
        return volumioMediaRepository.findByAlbumArtistAndAlbumTitleAndTrackTitleAndTrackNumber(artistName, albumName, trackTitle, trackIndex);
    }

    @Override
    public boolean existAndEquals(VolumioMedia media) {
        return volumioMediaRepository.findById(media.getTrackUri())
                .map(m -> m.equals(media))
                .orElse(false);
    }

    public boolean existByAlbumUri(String uri) {
        return volumioMediaRepository.findFirstByAlbumUri(uri).isPresent();
    }

    public boolean exist(String artist, String album) {
        return !volumioMediaRepository.findByAlbumArtistAndAlbumTitle(artist, album).isEmpty();
    }

    @Override
    public VolumioMedia findById(String id) {
        return volumioMediaRepository.findById(id).orElseThrow(() ->  new RuntimeException("not found"));
    }

    @Override
    public List<VolumioMedia> findAll() {
        return volumioMediaRepository.findAll();
    }

    @Override
    public boolean exist(VolumioMedia volumioMedia) {
        return volumioMediaRepository.findById(volumioMedia.getTrackUri()).isPresent();
    }

    public int syncTidalReleaseDate() {
        List<VolumioMedia> byAlbumReleaseDateIsNull = volumioMediaRepository.findByAlbumReleaseDateIsNull();
        AtomicInteger count = new AtomicInteger(0);
        byAlbumReleaseDateIsNull.forEach(volumioMedia -> {
            String uri = volumioMedia.getTrackUri();
            Optional<TidalMedia> tidalMedia = tidalMediaService.getById(uri.substring(uri.lastIndexOf("/") + 1));
            if (tidalMedia.isPresent()) {
                volumioMedia.setAlbumReleaseDate(tidalMedia.get().getReleaseDate());
                volumioMediaRepository.save(volumioMedia);
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    public void syncTrackArtistAndAlbumArtist() {
        List<VolumioMedia> byAlbumReleaseDateIsNull = volumioMediaRepository.findAll();
        byAlbumReleaseDateIsNull.stream().filter(volumioMedia ->
                (StringUtils.isBlank(volumioMedia.getAlbumArtist()) || volumioMedia.getAlbumArtist().trim().equals("*"))
                        && StringUtils.isNotBlank(volumioMedia.getTrackArtist()))
                .forEach(volumioMedia -> {
                    log.info("{} -> {}", volumioMedia.getAlbumArtist(), volumioMedia.getTrackArtist());
                    volumioMedia.setAlbumArtist(volumioMedia.getTrackArtist());
                    volumioMediaRepository.save(volumioMedia);
                });
    }


    public void syncPlexDate() {
        List<VolumioMedia> allVolumio = volumioMediaRepository.findAll();
        allVolumio.forEach(volumioMedia -> {
            List<MediaDistance> perfectMediaDistances = mediaDistanceRepository.findByFromIdAndFromClazzAndMatchStatus(volumioMedia.getTrackUri(), VolumioMedia.class.getName(), MatchStatus.PERFECT_MATCH.name());
            if (!perfectMediaDistances.isEmpty()) {
                updateVolumioMedia(volumioMedia, perfectMediaDistances);
            } else {
                List<MediaDistance> autoMediaDistances = mediaDistanceRepository.findByFromIdAndFromClazzAndMatchStatus(volumioMedia.getTrackUri(), VolumioMedia.class.getName(), MatchStatus.AUTOMATIC_MATCH.name());
                if (!autoMediaDistances.isEmpty()) {
                    updateVolumioMedia(volumioMedia, autoMediaDistances);
                }
            }
        });
    }

    private void updateVolumioMedia(VolumioMedia volumioMedia, List<MediaDistance> mediaDistances) {
        Optional<MediaDistance> to = mediaDistances.stream().filter(mediaDistance -> mediaDistance.getTo().getClazz().equals(PlexMedia.class.getName()))
                .findFirst();
        if (to.isPresent()) {

            PlexMedia plexMedia = plexMediaService.findById(to.get().getTo().getId());
            boolean updated = false;
            if (volumioMedia.getAlbumReleaseDate() == null && plexMedia.getAlbumOriginallyAvailableAt() != null) {
                volumioMedia.setAlbumReleaseDate(plexMedia.getAlbumOriginallyAvailableAt());
                updated = true;
            }
            if (volumioMedia.getAddedDate() == null & plexMedia.getAddedAt() != null) {
                volumioMedia.setAddedDate(plexMedia.getAddedAt().toLocalDate());
                updated = true;
            }
            if (updated) {
                volumioMediaRepository.save(volumioMedia);
            }
        }
    }

    public List<VolumioMedia> updateTidalReleaseDate() {
        return volumioMediaRepository.findByAlbumReleaseDateIsNullAndTrackType("tidal");

    }

    public void syncTidalDateFromFile() {
        try (Stream<String> lines = Files.lines(Paths.get("/Users/alexisperreault/Documents/music-central/script/target/out_.cvs"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                String[] split = line.split(";");
                Optional<VolumioMedia> byId = volumioMediaRepository.findById(split[0]);
                if (byId.isPresent()){
                    VolumioMedia volumioMedia = byId.get();
                    int year = Integer.valueOf(split[1].substring(0,4));
                    int month = Integer.valueOf(split[1].substring(5,7));
                    int day = Integer.valueOf(split[1].substring(8,10));
                    volumioMedia.setAlbumReleaseDate(LocalDate.of(year, month, day));
                    volumioMediaRepository.save(volumioMedia);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
