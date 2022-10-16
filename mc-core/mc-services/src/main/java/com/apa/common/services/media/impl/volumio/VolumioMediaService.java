package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.media.MediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class VolumioMediaService extends AbstractMediaService<VolumioMedia> implements MediaService<VolumioMedia> {

    private final VolumioMediaRepository volumioMediaRepository;

    private final TidalMediaService tidalMediaService;

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


}
