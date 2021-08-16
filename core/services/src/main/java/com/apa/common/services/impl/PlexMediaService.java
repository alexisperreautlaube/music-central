package com.apa.common.services.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.repositories.PlexMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlexMediaService extends AbstractMediaService<PlexMedia> {
    private final PlexMediaRepository plexMediaRepository;

    @Override
    @Transactional
    public VersionMedia<PlexMedia> save(PlexMedia plexMedia) {
        PlexMedia save = plexMediaRepository.save(plexMedia);
        return new VersionMedia(getEntityVersion(save), save);
    }

    @Override
    public void delete(UUID uuid) {
        plexMediaRepository.deleteById(uuid);
    }
}
