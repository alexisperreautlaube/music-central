package com.apa.consumer.services.impl;

import com.apa.common.msg.InputMessage;
import com.apa.common.msg.InputMessageEvent;
import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.TidalMediaDto;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.executor.impl.PlexMediaImporter;
import com.apa.events.executor.impl.TidalMediaImporter;
import com.apa.events.executor.impl.VolumioMediaImporter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class InputMessageConsumer implements ConsumerSeekAware {

    @Autowired
    private TidalMediaImporter tidalMediaImporter;

    @Autowired
    private PlexMediaImporter plexMediaImporter;

    @Autowired
    private VolumioMediaImporter volumioMediaImporter;

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.forEach((t, o) -> callback.seekToBeginning(t.topic(), t.partition()));
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.input.message}",
            containerFactory = "tidalMediaDtoKafkaListenerContainerFactory")
    public void doImport(InputMessage inputMessage) {
        InputMessageEvent inputMessageEvent = InputMessageEvent.valueOf(inputMessage.getEvent());
        log.debug("inputMessageEvent={}", inputMessageEvent);
        Gson gson = new Gson();
        switch (inputMessageEvent) {
            case IMPORT_TIDAL:
                TidalMediaDto tidalMediaDto = gson.fromJson(inputMessage.getData(), TidalMediaDto.class);
                tidalMediaImporter.execute(tidalMediaDto);
                break;
            case IMPORT_PLEX:
                PlexMediaDto plexMediaDto = gson.fromJson(inputMessage.getData(), PlexMediaDto.class);
                plexMediaImporter.execute(plexMediaDto);
                break;
            case IMPORT_VOLUMIO:
                VolumioMediaDto volumioMediaDto = gson.fromJson(inputMessage.getData(), VolumioMediaDto.class);
                volumioMediaImporter.execute(volumioMediaDto);
                break;
            default:
                throw new RuntimeException("not implemented yet");
                //break;
        }

        ;
    }
}
