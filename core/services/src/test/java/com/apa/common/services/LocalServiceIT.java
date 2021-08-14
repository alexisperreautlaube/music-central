package com.apa.common.services;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.LocalMedia;
import com.apa.common.repositories.LocalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalServiceIT extends AbstractCommonIT {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private LocalService localService;

    @Test
    public void saveTest() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        LocalMedia localMedia = localService.save(media);
        assertNotNull(localMedia);
    }
}