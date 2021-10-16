package com.apa.events.executor.impl;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.AbstractEventIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VolumioMediaImporterIT extends AbstractEventIT {

    @Autowired
    private VolumioMediaImporter volumioMediaImporter;

    @Autowired
    private VolumioMediaRepository volumioMediaRepository;



    @BeforeEach
    public void setup() {
        volumioMediaRepository.deleteAll();
    }

    @Test
    public void simpleTest() {
        VolumioMediaDto volumioMediaDto = VolumioMediaDto.builder()
                .trackUri("localId")
                .albumArtist("artist")
                .albumTitle("album")
                .trackTitle("title")
                .build();

        volumioMediaImporter.execute(volumioMediaDto);

        // media
        List<VolumioMedia> all = volumioMediaRepository.findAll();
        assertEquals(1, all.size());
        //assertEquals(volumioMediaDto, all.get(0));

    }
}