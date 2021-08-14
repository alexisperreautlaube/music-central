package com.apa.common.services;

import com.apa.common.entities.LocalMedia;
import com.apa.common.repositories.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalService implements MediaService<LocalMedia> {
    private final LocalRepository localRepository;

    @Override
    public LocalMedia save(LocalMedia localMedia) {
        return localRepository.save(localMedia);
    }
}
