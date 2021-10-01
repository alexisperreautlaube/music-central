package com.apa.common.services.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalMediaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalMediaServiceIT extends AbstractCommonIT {

    @Autowired
    private LocalMediaService localMediaService;

    @Autowired
    private LocalMediaRepository localMediaRepository;

    @Test
    public void saveTest() {
        LocalMedia media = LocalMedia.builder()
                .localId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();

        LocalMedia localMedia =  localMediaService.save(media);
        assertNotNull(localMedia);
    }
}