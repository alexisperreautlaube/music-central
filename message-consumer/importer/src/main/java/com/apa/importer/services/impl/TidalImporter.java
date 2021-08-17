package com.apa.importer.services.impl;

import com.apa.importer.dto.TidalMediaDto;
import com.apa.importer.services.Importer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TidalImporter implements Importer<TidalMediaDto> {

    @Override
    @KafkaListener(id = "tidalImporter", topics = "tidal.importer")
    public void doImport(TidalMediaDto media) {

    }
}
