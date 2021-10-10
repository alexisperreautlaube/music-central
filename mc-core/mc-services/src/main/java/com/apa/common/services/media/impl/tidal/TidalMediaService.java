package com.apa.common.services.media.impl.tidal;

import com.apa.common.entities.media.TidalMedia;
import com.apa.common.repositories.TidalMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean existAndEquals(TidalMedia media) {
        return tidalMediaRepository.findById(media.getTidalTrackId())
                .map(m -> m.equals(media))
                .orElse(false);
    }

}
