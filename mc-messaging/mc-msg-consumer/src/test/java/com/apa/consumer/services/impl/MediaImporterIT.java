package com.apa.consumer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.InputMessageEvent;
import com.apa.common.repositories.PlexMediaRepository;
import com.apa.common.repositories.TidalMediaRepository;
import com.apa.consumer.AbstractConsumerIT;
import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.TidalMediaDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MediaImporterIT extends AbstractConsumerIT {

    @Value("${spring.kafka.topic.import.message}")
    private String topic;

    @Autowired
    private TidalMediaRepository tidalMediaRepository;

    @Autowired
    private PlexMediaRepository plexMediaRepository;

    @Autowired
    private KafkaTemplate<String, InputMessage> inputMessageTemplate;

    @Test
    void shouldBeRunningKafka() {
        assertTrue(getKafka().isRunning());
    }

    @Test
    public void importTidal() throws InterruptedException {
        Gson gson = new Gson();
        TidalMediaDto tidalMediaDto = TidalMediaDto.builder()
                .tidalTrackId(UUID.randomUUID().toString())
                .releaseDate(LocalDate.now())
                .build();

        InputMessage inputMessage = InputMessage.builder()
                .event(InputMessageEvent.IMPORT_TIDAL.toString())
                .data(gson.toJson(tidalMediaDto))
                .build();

                inputMessageTemplate.send(topic, inputMessage);
        List<TidalMedia> all = tidalMediaRepository.findAll();
        int timeOut = 5000;
        while (all.isEmpty() && timeOut > 0) {
            timeOut = timeOut - 50;
            Thread.sleep(50);
            all = tidalMediaRepository.findAll();
        }
        assertFalse(all.isEmpty());
        assertEquals(all.get(0).getTidalTrackId(), tidalMediaDto.getTidalTrackId());
    }

    @Test
    public void importPlex() throws InterruptedException {
        Gson gson = new Gson();
        PlexMediaDto tidalMediaDto = PlexMediaDto.builder()
                .plexId(UUID.randomUUID().toString())
                .addedAt(LocalDateTime.now())
                .build();

        InputMessage inputMessage = InputMessage.builder()
                .event(InputMessageEvent.IMPORT_PLEX.toString())
                .data(gson.toJson(tidalMediaDto))
                .build();

                inputMessageTemplate.send(topic, inputMessage);
        List<PlexMedia> all = plexMediaRepository.findAll();
        int timeOut = 5000;
        while (all.isEmpty() && timeOut > 0) {
            timeOut = timeOut - 50;
            Thread.sleep(50);
            all = plexMediaRepository.findAll();
        }
        assertFalse(all.isEmpty());
        assertEquals(all.get(0).getPlexId(), tidalMediaDto.getPlexId());
    }
}