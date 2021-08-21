package com.apa.common.services.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalMediaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
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
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        VersionMedia<LocalMedia> versionMedia = localMediaService.save(media);
        LocalMedia localMedia = versionMedia.getMedia();
        assertNotNull(localMedia);
        assertEquals(1, versionMedia.getVersion());
    }

    @Test
    public void saveSaveTest() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        VersionMedia<LocalMedia> versionMedia = localMediaService.save(media);
        LocalMedia localMedia = versionMedia.getMedia();
        assertNotNull(localMedia);
        assertEquals(1, versionMedia.getVersion());
        localMedia.setTrackTitle("title2");
        versionMedia = localMediaService.save(localMedia);
        assertEquals(2, versionMedia.getVersion());
    }

    @Test
    public void restoreTest() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        VersionMedia<LocalMedia> versionMedia = localMediaService.save(media);
        LocalMedia localMedia = versionMedia.getMedia();
        assertNotNull(localMedia);
        assertEquals(1, versionMedia.getVersion());
        localMedia.setTrackTitle("title2");
        versionMedia = localMediaService.save(localMedia);
        assertEquals(2, versionMedia.getVersion());
        assertEquals("title2", versionMedia.getMedia().getTrackTitle());
        versionMedia = localMediaService.restore(versionMedia.getMedia().getUuid(), 1);
        assertEquals("title", versionMedia.getMedia().getTrackTitle());
    }

    @Test
    public void restore0Test() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        VersionMedia<LocalMedia> versionMedia = localMediaService.save(media);
        LocalMedia localMedia = versionMedia.getMedia();
        assertNotNull(localMedia);
        assertEquals(1, versionMedia.getVersion());
        versionMedia = localMediaService.restore(versionMedia.getMedia().getUuid(), 0);
        assertNull(versionMedia.getMedia());
        assertEquals(localMediaRepository.findById(media.getUuid()), Optional.empty());
    }
}