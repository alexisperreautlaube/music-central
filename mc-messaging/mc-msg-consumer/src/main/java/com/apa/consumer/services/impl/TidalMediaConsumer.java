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

    @KafkaListener(groupId = "media.importer", topics = "tidal.media.importer", containerFactory = "tidalMediaDtoKafkaListenerContainerFactory")
    public void doImport(TidalMediaDto tidalMediaDto) {
        tidalMediaImporter.execute(tidalMediaDto);
    }
}
