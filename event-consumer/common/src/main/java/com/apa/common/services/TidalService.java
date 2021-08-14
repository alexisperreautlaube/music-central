package com.apa.common.services;

import com.apa.common.entities.TidalMedia;
import com.apa.common.repositories.TidalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TidalService implements MediaService<TidalMedia> {

    private final TidalRepository tidalRepository;

    @Override
    public TidalMedia save(TidalMedia tidalMedia) {
        return tidalRepository.save(tidalMedia);
    }
}
