package com.apa.importer.services.impl;

import com.apa.importer.dto.LocalMediaDto;
import com.apa.importer.services.Importer;
import org.springframework.kafka.annotation.KafkaListener;

public class LocalImporter implements Importer<LocalMediaDto> {
    @Override
    @KafkaListener(id = "localImporter", topics = "local.importer")
    public void doImport(LocalMediaDto media) {

    }
}
