package com.apa.common.services.impl;

import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocalMediaService extends AbstractMediaService<LocalMedia> {

    private final LocalMediaRepository localMediaRepository;

    @Override
    @Transactional
    public LocalMedia save(LocalMedia localMedia) {
        return localMediaRepository.save(localMedia);
    }

    @Override
    public void delete(String uuid) {
        localMediaRepository.deleteById(uuid);
    }

    @Override
    public boolean existAndEquals(LocalMedia media) {
        return getByLocalId(media.getLocalId())
                .map(m -> m.equals(media))
                .orElse(false);
    }

    public Optional<LocalMedia> getByLocalId(String localId) {
        return localMediaRepository.findByLocalId(localId);
    }
}
