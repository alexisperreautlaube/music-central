package com.apa.importer.services.impl;

import com.apa.importer.dto.PlexMediaDto;
import com.apa.importer.services.Importer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PlexImporter implements Importer<PlexMediaDto> {
    @Override
    @KafkaListener(id = "plexImporter", topics = "plex.importer")
    public void doImport(PlexMediaDto media) {

    }
}
