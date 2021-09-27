package com.apa.consumer.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.List;

@Slf4j
public class McKafkaErrorHandler implements ErrorHandler {
    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        String s = thrownException.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];
        String topics = s.split("-")[0];
        int offset = Integer.valueOf(s.split("offset ")[1]);
        int partition = Integer.valueOf(s.split("-")[1].split(" at")[0]);

        TopicPartition topicPartition = new TopicPartition(topics, partition);
        log.error(thrownException.getMessage(), thrownException);
        log.info("Skipping " + topics + "-" + partition + " offset " + offset);
        consumer.seek(topicPartition, offset + 1);
    }

    @Override
    public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord) {
        log.info("HUmm ");
    }

    @Override
    public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord, Consumer<?,?> consumer) {
        String topic = consumerRecord.topic();
        long offset = consumerRecord.offset();
        int partition = consumer.partitionsFor(topic).get(0).partition();

        TopicPartition topicPartition = new TopicPartition(topic, partition);
        log.error(e.getMessage(), e);
        log.info("Skipping " + topic + "-" + partition + " offset " + offset);
        consumer.seek(topicPartition, offset + 1);
    }
}
