package com.apa.common.services;

import com.apa.common.entities.PlexMedia;
import com.apa.common.repositories.PlexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlexService implements MediaService<PlexMedia> {
    private final PlexRepository plexRepository;

    @Override
    public PlexMedia save(PlexMedia plexMedia) {
        return plexRepository.save(plexMedia);
    }
}
