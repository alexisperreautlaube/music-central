package com.apa.common.services.media.impl.tidal;

import com.apa.common.entities.media.TidalMedia;
import com.apa.common.repositories.TidalMediaRepository;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TidalMediaService extends AbstractMediaService<TidalMedia> implements MediaService<TidalMedia> {

    private final TidalMediaRepository tidalMediaRepository;

    @Override
    @Transactional
    public TidalMedia save(TidalMedia tidalMedia) {
        return tidalMediaRepository.save(tidalMedia);
    }

    @Override
    public List<TidalMedia> findPerfectMatch(String artistName, String albumName, String trackTitle, String trackIndex) {
        if (StringUtils.isBlank(trackIndex)) {
            return List.of();
        }
        return tidalMediaRepository.findByArtistNameAndAlbumNameAndTrackTitleAndTrackNumber(artistName, albumName, trackTitle, Integer.valueOf(trackIndex));
    }

    @Override
     public boolean existAndEquals(TidalMedia media) {
        return getById(media)
                .map(m -> m.equals(media))
                .orElse(false);
    }

    private Optional<TidalMedia> getById(TidalMedia media) {
        return tidalMediaRepository.findById(media.getTidalTrackId());
    }

    @Override
    public List<TidalMedia> findAll() {
        return tidalMediaRepository.findAll();
    }

    @Override
    public boolean exist(TidalMedia media) {
        return getById(media).isPresent();
    }
}
