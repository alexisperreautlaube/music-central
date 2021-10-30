package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VolumioMediaService extends AbstractMediaService<VolumioMedia> implements MediaService<VolumioMedia> {

    private final VolumioMediaRepository volumioMediaRepository;

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
}
