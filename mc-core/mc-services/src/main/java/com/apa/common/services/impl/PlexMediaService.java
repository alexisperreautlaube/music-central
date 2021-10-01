package com.apa.common.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.repositories.PlexMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PlexMediaService extends AbstractMediaService<PlexMedia> {

    private final PlexMediaRepository plexMediaRepository;

    @Override
    @Transactional
    public PlexMedia save(PlexMedia plexMedia) {
        return plexMediaRepository.save(plexMedia);
    }

    @Override
    public void delete(String uuid) {
        plexMediaRepository.deleteById(uuid);
    }

    @Override
    public boolean existAndEquals(PlexMedia media) {
        throw new RuntimeException("not implemted yet");
    }

}
