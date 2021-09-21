package com.apa.consumer.services.impl;

import com.apa.core.dto.media.TidalMediaDto;
import com.apa.events.executor.impl.TidalMediaImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TidalMediaConsumer {

    @Autowired
    private TidalMediaImporter tidalMediaImporter;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.tidal.media.importer}", containerFactory = "tidalMediaDtoKafkaListenerContainerFactory")
    public void doImport(TidalMediaDto tidalMediaDto) {
        tidalMediaImporter.execute(tidalMediaDto);
    }
}
