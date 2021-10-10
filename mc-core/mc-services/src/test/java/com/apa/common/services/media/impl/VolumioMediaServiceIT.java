package com.apa.common.services.media.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VolumioMediaServiceIT extends AbstractCommonIT {

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private VolumioMediaRepository volumioMediaRepository;

    @Test
    public void saveTest() {
        VolumioMedia media = VolumioMedia.builder()
                .trackUri(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumTitle("album")
                .trackArtist("artist")
                .build();

        VolumioMedia volumioMedia =  volumioMediaService.save(media);
        assertNotNull(volumioMedia);
    }
}