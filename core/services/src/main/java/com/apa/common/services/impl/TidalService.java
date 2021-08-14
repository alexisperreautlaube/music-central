package com.apa.common.services.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.repositories.TidalRepository;
import com.apa.common.services.AbstractMediaServices;
import com.apa.common.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TidalService extends AbstractMediaServices<TidalMedia> implements MediaService<TidalMedia> {

    private final TidalRepository tidalRepository;

    @Override
    @Transactional
    public VersionMedia<TidalMedia> save(TidalMedia tidalMedia) {
        TidalMedia save = tidalRepository.save(tidalMedia);
        return new VersionMedia(getEntityVersion(save), save);
    }
}
