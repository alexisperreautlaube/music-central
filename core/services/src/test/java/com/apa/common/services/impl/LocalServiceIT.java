package com.apa.common.services.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalRepository;
import com.apa.common.services.impl.LocalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalServiceIT extends AbstractCommonIT {

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
        VersionMedia<LocalMedia> versionMedia = localService.save(media);
        LocalMedia localMedia = versionMedia.getMedia();
        assertNotNull(localMedia);
        assertEquals(1, versionMedia.getVersion());
    }

    @Test
    public void saveSaveTest() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        VersionMedia<LocalMedia> versionMedia = localService.save(media);
        LocalMedia localMedia = versionMedia.getMedia();
        assertNotNull(localMedia);
        assertEquals(1, versionMedia.getVersion());
        localMedia.setTitle("title2");
        versionMedia = localService.save(localMedia);
        assertEquals(2, versionMedia.getVersion());
    }
}