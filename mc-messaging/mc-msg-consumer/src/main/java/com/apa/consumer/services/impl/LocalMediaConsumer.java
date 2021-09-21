package com.apa.consumer.services.impl;

import com.apa.core.dto.media.LocalMediaDto;
import com.apa.events.executor.impl.LocalMediaImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LocalMediaConsumer {

    @Autowired
    private LocalMediaImporter localMediaImporter;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.local.media.importer}", containerFactory = "localMediaDtoKafkaListenerContainerFactory")
    public void doImport(LocalMediaDto localMediaDto) {
        localMediaImporter.execute(localMediaDto);
    }
}
