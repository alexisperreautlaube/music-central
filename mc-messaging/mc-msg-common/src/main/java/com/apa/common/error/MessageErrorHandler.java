package com.apa.common.error;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.GenericErrorHandler;

public class MessageErrorHandler implements GenericErrorHandler<ConsumerRecord<?, ?>> {

    @Autowired
    private ErrorMessageService errorMessageService;

    @Override
    public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord) {
        errorMessageService.save(ErrorMessage.builder()
                .topic(consumerRecord.topic())
                .payload(consumerRecord.key())
                .exception(e)
                .build());
    }
}
