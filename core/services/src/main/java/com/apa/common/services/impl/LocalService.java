package com.apa.common.services.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalRepository;
import com.apa.common.services.AbstractMediaServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LocalService extends AbstractMediaServices<LocalMedia>  {

    private final LocalRepository localRepository;

    @Override
    @Transactional
    public VersionMedia<LocalMedia> save(LocalMedia localMedia) {
        LocalMedia save = localRepository.save(localMedia);
        return new VersionMedia(getEntityVersion(save), save);
    }
}
