package com.apa.importer.services.impl;

import com.apa.AbstractImporterIT;
import com.apa.common.repositories.LocalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
class LocalImporterIT extends AbstractImporterIT {

    @Autowired
    private LocalRepository localRepository;
/*
    @Autowired
    private KafkaTemplate<String, LocalMediaDto> template;
*/
    @Test
    public void importLocal() {
/*
        LocalMediaDto build = LocalMediaDto.builder()
                .localId(UUID.randomUUID().toString())
                .build();

        template.send("local.importer", build);
        assertEquals(localRepository.findAll().get(0).getLocalId(), build.getLocalId());
        */

    }


}