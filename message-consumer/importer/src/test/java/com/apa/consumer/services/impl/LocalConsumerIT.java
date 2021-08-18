package com.apa.consumer.services.impl;

import com.apa.common.repositories.LocalMediaRepository;
import com.apa.core.dto.media.LocalMediaDto;
import com.apa.consumer.AbstractConsumerIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalConsumerIT extends AbstractConsumerIT {

    @Autowired
    private LocalMediaRepository localMediaRepository;

    @Autowired
    private KafkaTemplate<String, LocalMediaDto> localMediaDtoTemplate;

    @Autowired
    private LocalMediaConsumer localMediaConsumer;

    @Test
    void shouldBeRunningKafka() throws Exception {
        assertTrue(getKafka().isRunning());
    }

    @Test
    public void importLocal() throws InterruptedException {

        LocalMediaDto build = LocalMediaDto.builder()
                .localId(UUID.randomUUID().toString())
                .build();

        localMediaDtoTemplate.send("local.media.importer", build);

        Thread.sleep(5000);

        assertEquals(localMediaRepository.findAll().get(0).getLocalId(), build.getLocalId());

    }
}