package com.apa.consumer.services.impl;

import com.apa.common.entities.enums.RatingType;
import com.apa.common.msg.InputMessage;
import com.apa.common.services.media.impl.RatingService;
import com.apa.core.dto.media.RatingDto;
import com.apa.events.mapper.RatingMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.apa.common.entities.enums.RatingType.*;

@Slf4j
@Component
public class RateMessageConsumer implements ConsumerSeekAware {

    @Autowired
    private RatingService ratingService;

    @Value("${spring.kafka.topic.rate.resetOffset}")
    private Boolean resetOffset;

    @Value("${spring.kafka.topic.rate.message}")
    private String topic;

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        if (Boolean.TRUE.equals(resetOffset)) {
            assignments.forEach((t, o) -> {
                if (topic.equals(t.topic())) {
                    callback.seekToBeginning(t.topic(), t.partition());
                }
            });
        }
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.rate.message}",
            containerFactory = "importMediaDtoKafkaListenerContainerFactory",
            concurrency = "1"
    )
    public void doImport(InputMessage inputMessage) {
        RatingType ratingType = valueOf(inputMessage.getEvent());
        log.debug("ratingType={}", ratingType);
        log.debug("inputMessage={}", inputMessage);
        Gson gson = new Gson();
        switch (ratingType) {
            case VOLUMIO_MANUAL:
                RatingDto ratingDto = gson.fromJson(inputMessage.getData(), RatingDto.class);
                ratingService.save(RatingMapper.toRating(ratingDto, ratingType));
                break;
            default:
                throw new RuntimeException("not implemented yet");
        }
    }
}
