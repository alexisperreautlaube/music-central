package com.apa.importer.services.impl;

import com.apa.events.executor.impl.LocalMediaImporter;
import com.apa.importer.dto.LocalMediaDto;
import com.apa.importer.services.Importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LocalImporter implements Importer<LocalMediaDto> {

    @Autowired
    private LocalMediaImporter localMediaImporter;

    @Override
    @KafkaListener(id = "localMediaImporter", topics = "local.importer")
    public void doImport(LocalMediaDto media) {

    }
}
