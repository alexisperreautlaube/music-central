package com.apa.importer.services.impl;

import com.apa.common.repositories.LocalMediaRepository;
import com.apa.importer.AbstractImporterIT;
import com.apa.importer.dto.LocalMediaDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class LocalImporterIT extends AbstractImporterIT {

    @Autowired
    private LocalMediaRepository localMediaRepository;

    @Autowired
    private KafkaTemplate<String, LocalMediaDto> localMediaDtoTemplate;

    @Test
    public void importLocal() {

        LocalMediaDto build = LocalMediaDto.builder()
                .localId(UUID.randomUUID().toString())
                .build();

        localMediaDtoTemplate.send("local.media.importer", build);
        assertEquals(localMediaRepository.findAll().get(0).getLocalId(), build.getLocalId());

    }
}