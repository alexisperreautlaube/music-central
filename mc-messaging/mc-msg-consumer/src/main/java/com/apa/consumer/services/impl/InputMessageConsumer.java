package com.apa.consumer.services.impl;

import com.apa.common.msg.InputMessage;
import com.apa.common.msg.InputMessageEvent;
import com.apa.core.dto.media.TidalMediaDto;
import com.apa.events.executor.impl.TidalMediaImporter;
import com.google.gson.Gson;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InputMessageConsumer implements ConsumerSeekAware {

    @Autowired
    private TidalMediaImporter tidalMediaImporter;

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
        switch (inputMessageEvent) {
            case IMPORT_TIDAL:
                Gson gson = new Gson();
                TidalMediaDto tidalMediaDto = gson.fromJson(inputMessage.getData(), TidalMediaDto.class);
                tidalMediaImporter.execute(tidalMediaDto);
                break;
            case IMPORT_PLEX:
                throw new RuntimeException("not implemented yet");
                //break;
            case IMPORT_LOCAL:
                throw new RuntimeException("not implemented yet");
                //break;
            default:
                throw new RuntimeException("not implemented yet");
                //break;
        }

        ;
    }
}
