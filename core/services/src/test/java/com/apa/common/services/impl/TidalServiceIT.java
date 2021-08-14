package com.apa.common.services.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.repositories.TidalRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class TidalServiceIT extends AbstractCommonIT {

    @Autowired
    private TidalService tidalService;

    @Test
    public void saveTest() {
        TidalMedia media = TidalMedia.builder()
                .uuid(UUID.randomUUID())
                .tidalId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        TidalMedia tidalMedia = tidalService.save(media).getMedia();
        assertNotNull(tidalMedia);
        log.info("patate");
    }
}