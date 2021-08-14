package com.apa.common.services;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.PlexMedia;
import com.apa.common.repositories.PlexRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlexServiceIT extends AbstractCommonIT {

    @Autowired
    private PlexRepository plexRepository;

    @Autowired
    private PlexService plexService;

    @Test
    public void saveTest() {
        PlexMedia media = PlexMedia.builder()
                .uuid(UUID.randomUUID())
                .plexId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        PlexMedia plexMedia = plexService.save(media);
        assertNotNull(plexMedia);
    }
}