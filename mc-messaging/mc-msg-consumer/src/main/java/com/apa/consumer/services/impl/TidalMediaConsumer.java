package com.apa.consumer.services.impl;

import com.apa.core.dto.media.TidalMediaDto;
import com.apa.events.executor.impl.TidalMediaImporter;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TidalMediaConsumer implements ConsumerSeekAware {

    @Autowired
    private TidalMediaImporter tidalMediaImporter;

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.forEach((t, o) -> callback.seekToBeginning(t.topic(), t.partition()));
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.tidal.media.importer}",
            containerFactory = "tidalMediaDtoKafkaListenerContainerFactory")
    public void doImport(TidalMediaDto tidalMediaDto) {
        tidalMediaImporter.execute(tidalMediaDto);
    }
}
