package com.apa.consumer.services.impl;

import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalMediaRepository;
import com.apa.core.dto.media.LocalMediaDto;
import com.apa.consumer.AbstractConsumerIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalConsumerIT extends AbstractConsumerIT {

    @Value("${spring.kafka.topic.local.media.importer}")
    private String topic;

    @Autowired
    private LocalMediaRepository localMediaRepository;

    @Autowired
    private KafkaTemplate<String, LocalMediaDto> localMediaDtoTemplate;

    @Test
    void shouldBeRunningKafka() {
        assertTrue(getKafka().isRunning());
    }

    @Test
    public void importLocal() throws InterruptedException {

        LocalMediaDto build = LocalMediaDto.builder()
                .localId(UUID.randomUUID().toString())
                .build();

        localMediaDtoTemplate.send(topic, build);
        List<LocalMedia> all = localMediaRepository.findAll();
        int timeOut = 5000;
        while (all.isEmpty() && timeOut > 0) {
            timeOut = timeOut - 50;
            Thread.sleep(50);
            all = localMediaRepository.findAll();
        }
        assertEquals(all.get(0).getLocalId(), build.getLocalId());
    }
}