package com.apa.common.services.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.repositories.PlexRepository;
import com.apa.common.services.AbstractMediaServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PlexService extends AbstractMediaServices<PlexMedia> {
    private final PlexRepository plexRepository;

    @Override
    @Transactional
    public VersionMedia<PlexMedia> save(PlexMedia plexMedia) {
        PlexMedia save = plexRepository.save(plexMedia);
        return new VersionMedia(getEntityVersion(save), save);
    }
}
