package com.apa.consumer.services.impl;

import com.apa.core.dto.media.PlexMediaDto;
import com.apa.events.executor.impl.LocalMediaImporter;
import com.apa.events.executor.impl.PlexMediaImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PlexMediaConsumer {

    @Autowired
    private PlexMediaImporter plexMediaImporter;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.plex.media.importer}", containerFactory = "plexMediaDtoKafkaListenerContainerFactory")
    public void doImport(PlexMediaDto plexMediaDto) {
        plexMediaImporter.execute(plexMediaDto);
    }
}
