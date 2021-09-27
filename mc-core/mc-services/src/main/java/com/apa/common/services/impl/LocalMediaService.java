package com.apa.common.services.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalMediaService extends AbstractMediaService<LocalMedia> {

    private final LocalMediaRepository localMediaRepository;

    @Override
    @Transactional
    public VersionMedia<LocalMedia> save(LocalMedia localMedia) {
        LocalMedia save = localMediaRepository.save(localMedia);
        return new VersionMedia(getEntityVersion(save), save);
    }

    @Override
    public void delete(String uuid) {
        localMediaRepository.deleteById(uuid);
    }

    @Override
    public boolean existAndEquals(LocalMedia media) {
        return true;
    }

}
