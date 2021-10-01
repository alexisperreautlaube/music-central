package com.apa.common.services.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.TidalMedia;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class TidalMediaServiceIT extends AbstractCommonIT {

    @Autowired
    private TidalMediaService tidalMediaService;

    @Test
    public void saveTest() {
        TidalMedia media = TidalMedia.builder()
                .uuid(UUID.randomUUID().toString())
                .tidalTrackId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        TidalMedia tidalMedia = tidalMediaService.save(media);
        assertNotNull(tidalMedia);
    }
}