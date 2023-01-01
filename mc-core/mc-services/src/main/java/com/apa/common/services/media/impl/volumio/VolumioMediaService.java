package com.apa.common.services.media.impl.volumio;

import com.apa.client.volumio.VolumioClient;
import com.apa.client.volumio.VolumioClientSong;
import com.apa.common.entities.enums.MediaErrorStatus;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.mapper.VolumioMediaMapper;
import com.apa.common.repositories.MediaDistanceRepository;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.media.AvailableMediasService;
import com.apa.common.services.media.MediaService;
import com.apa.common.services.media.impl.MediaErrorService;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.core.dto.media.VolumioMediaDto;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class VolumioMediaService extends AbstractMediaService<VolumioMedia> implements MediaService<VolumioMedia> {

    private static final List<String> EXCLUDE_TRACK_TYPE = List.of("avi", "mpg", "mov");

    private final VolumioMediaRepository volumioMediaRepository;

    private final TidalMediaService tidalMediaService;

    private final MediaDistanceRepository mediaDistanceRepository;

    private final PlexMediaService plexMediaService;

    private final VolumioClient volumioClient;

    private final MediaErrorService mediaErrorService;

    private final AvailableMediasService availableMediasService;
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

    public VolumioMedia doFindById(String id) {
        return findById(id);
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

    public boolean existingTrackMatch(String uri, String artist, String album) {
        Collection<? extends VolumioMediaDto> search = volumioClient.searchTrack(artist, album, this::getTidalTracks);
        if (!search.isEmpty()) {
            if (!existByAlbumUri(search.stream().findFirst().get().getAlbumUri())) {
                search.stream()
                        .forEach(m -> save(VolumioMediaMapper.toVolumioMedia(m)));
            }
            mediaErrorService.save(uri, VolumioMedia.class.getName(), artist, album, MediaErrorStatus.ERROR_WITH_ALBUM_AS_TRACK_IN_ALBUM);
            return true;
        }
        return false;
    }
    public boolean existingAlbumMatch(String uri, String artist, String album) {
        Collection<? extends VolumioMediaDto> search = volumioClient.search(artist, album, this::getTidalTracks);
        if (!search.isEmpty()) {
            if (!existByAlbumUri(search.stream().findFirst().get().getAlbumUri())) {
                search.stream()
                        .forEach(m -> save(VolumioMediaMapper.toVolumioMedia(m)));
            }
            mediaErrorService.save(uri, VolumioMedia.class.getName(), artist, album, MediaErrorStatus.ERROR_WITH_REPLACEMENT);
            return true;
        }
        return false;
    }

    public List<VolumioClientSong> getList() {
        List<String> triageIdList = availableMediasService.createTriageIdList();
        List<VolumioClientSong> volumioClientSongs = new ArrayList<>();
        triageIdList.stream().forEach(id -> {
            VolumioMedia byId = findById(id);
            volumioClientSongs.add(VolumioClientSong.builder()
                    .uri(byId.getTrackUri())
                    .service(byId.getAlbumTrackType().equals("tidal") ? "tidal" : "mpd")
                    .title(byId.getTrackTitle())
                    .artist(byId.getTrackArtist())
                    .album(byId.getAlbumTitle())
                    .type("song")
                    .trackNumber(0)
                    .duration(StringUtils.isNotBlank(byId.getTrackDuration()) ? Integer.valueOf(byId.getTrackDuration()) : 0)
                    .trackType(byId.getTrackType())
                    .build());
        });

        return volumioClientSongs;
    }

    public List<VolumioMediaDto> getNotSavedVolumioTidalAlbum() {
        List<VolumioMediaDto> list = volumioClient.getVolumioTidalMedias(this::getTidalTracks);

        return list.stream()
                .filter(v -> !existAndEquals(VolumioMediaMapper.toVolumioMedia(v)))
                .collect(Collectors.toList());
    }

    private Collection<? extends VolumioMediaDto> getTidalTracks(String uri) {
        return getTidalTracks(uri, null, null);
    }

    private Collection<? extends VolumioMediaDto> getTidalTracks(String uri, String artist, String album) {
        List<VolumioMediaDto> mediaDtos;
        try {
            mediaDtos = volumioClient.extracted(uri);
            mediaErrorService.remove(uri, VolumioMedia.class.getName());
            return mediaDtos;
        } catch (Throwable e) {
            if (artist != null && album != null) {
                if (mediaErrorService.doesNotExistOrExistWithErrorStatus(uri, VolumioMedia.class.getName())) {
                    boolean existingAlbumMatch = existingAlbumMatch(uri, artist, album);
                    if (!existingAlbumMatch) {
                        boolean existingTrackMatch = existingTrackMatch(uri, artist, album);
                        if (!existingTrackMatch) {
                            if (mediaErrorService.doesNotExistOrExistWithStatusElse(uri, VolumioMedia.class.getName(), MediaErrorStatus.ERROR)) {
                                log.error("uri={}, artist={}, album={}, uri={}", uri, artist, album, uri);
                                mediaErrorService.save(uri, VolumioMedia.class.getName(), artist, album, MediaErrorStatus.ERROR);
                            }
                        }
                    }
                }

            } else {
                log.debug("artist={}, album={}, uri={}", artist, album, uri);
            }
        }
        return List.of();
    }

    public List<VolumioMediaDto> getNotSavedVolumioLocalAlbum() {
        return volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus).stream()
                .filter(v -> !existAndEquals(VolumioMediaMapper.toVolumioMedia(v)))
                .filter(v -> !EXCLUDE_TRACK_TYPE.contains(v.getTrackType().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void createAvailableList() {
        availableMediasService.createAvailableList(findAll(), this::findById);
    }
}
