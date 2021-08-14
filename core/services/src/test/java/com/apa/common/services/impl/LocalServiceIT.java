package com.apa.common.services.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalServiceIT extends AbstractCommonIT {

    @Autowired
    private LocalService localService;

    @Autowired
    private LocalRepository localRepository;

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

    @Test
    public void restoreTest() {
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
        assertEquals("title2", versionMedia.getMedia().getTitle());
        versionMedia = localService.restore(versionMedia.getMedia(), 1);
        assertEquals("title", versionMedia.getMedia().getTitle());
    }

    @Test
    public void restore0Test() {
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
        versionMedia = localService.restore(versionMedia.getMedia(), 0);
        assertNull(versionMedia.getMedia());
        assertEquals(localRepository.findById(media.getUuid()), Optional.empty());
    }
}