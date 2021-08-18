package com.apa.consumer.services.impl;

import com.apa.core.dto.media.PlexMediaDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PlexMediaConsumer {
    @KafkaListener(groupId = "local.media.importer", topics = "plex.media.importer", containerFactory = "localMediaDtoKafkaListenerContainerFactory")
    public void doImport(PlexMediaDto plexMediaDto) {

    }
}
