package com.apa.consumer.services.impl;

import com.apa.core.dto.media.TidalMediaDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TidalMediaConsumer {

    @KafkaListener(groupId = "local.media.importer", topics = "tidal.media.importer", containerFactory = "localMediaDtoKafkaListenerContainerFactory")
    public void doImport(TidalMediaDto tidalMediaDto) {

    }
}
