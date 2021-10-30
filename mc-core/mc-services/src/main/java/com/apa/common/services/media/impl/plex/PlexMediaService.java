package com.apa.common.services.media.impl.plex;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.repositories.PlexMediaRepository;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlexMediaService extends AbstractMediaService<PlexMedia> implements MediaService<PlexMedia> {

    private final PlexMediaRepository plexMediaRepository;

    @Override
    @Transactional
    public PlexMedia save(PlexMedia plexMedia) {
        return plexMediaRepository.save(plexMedia);
    }

    @Override
    public boolean existAndEquals(PlexMedia media) {
        return plexMediaRepository.findById(media.getPlexId())
                .map(m -> m.equals(media))
                .orElse(false);
    }

    @Override
    public PlexMedia findById(String id) {
        return plexMediaRepository.findById(id).orElseThrow(() ->  new RuntimeException("not found"));
    }

    public List<PlexMedia> findPerfectMatch(String artistName, String albumName, String trackTitle, String trackIndex) {
        return plexMediaRepository.findByArtistNameAndAlbumNameAndTrackTitleAndTrackIndex(artistName, albumName, trackTitle, trackIndex);
    }

    @Override
    public List<PlexMedia> findAll() {
        return plexMediaRepository.findAll();
    }

    @Override
    public boolean exist(PlexMedia media) {
        return plexMediaRepository.findById(media.getPlexId()).isPresent();
    }
}
