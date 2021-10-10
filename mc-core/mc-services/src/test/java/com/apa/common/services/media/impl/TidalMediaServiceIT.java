package com.apa.common.services.media.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
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
                .tidalTrackId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        TidalMedia tidalMedia = tidalMediaService.save(media);
        assertNotNull(tidalMedia);
    }
}