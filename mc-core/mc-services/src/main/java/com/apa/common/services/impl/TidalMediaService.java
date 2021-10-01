package com.apa.common.services.impl;

import com.apa.common.entities.media.TidalMedia;
import com.apa.common.repositories.TidalMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TidalMediaService extends AbstractMediaService<TidalMedia> {

    private final TidalMediaRepository tidalMediaRepository;

    @Override
    @Transactional
    public TidalMedia save(TidalMedia tidalMedia) {
        return tidalMediaRepository.save(tidalMedia);
    }

    @Override
    public void delete(String uuid) {
        tidalMediaRepository.deleteById(uuid);
    }

    @Override
    public boolean existAndEquals(TidalMedia media) {
        return getByTidalTrackId(media.getTidalTrackId())
                .map(m -> m.equals(media))
                .orElse(false);
    }

    public Optional<TidalMedia> getByTidalTrackId(String tidalTrackId) {
        return tidalMediaRepository.findByTidalTrackId(tidalTrackId);
    }

}
