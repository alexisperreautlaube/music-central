package com.apa.producer.services.impl;

import com.apa.client.volumio.VolumioClient;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.impor.ImportMessageEvent;
import com.apa.core.dto.media.VolumioMediaDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VolumioNewSongImportProducer {

    @Value("${spring.kafka.topic.import.message}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, InputMessage> inputMessageTemplate;

    @Autowired
    private VolumioClient volumioClient;

    public void produceNewVolumioTrackMessage() {
        volumioClient.getNotSavedVolumioLocalAlbum().forEach(v -> produceAndSendMsg(v));
        volumioClient.getNotSavedVolumioTidalAlbum().forEach(v -> produceAndSendMsg(v));
    }

    private void produceAndSendMsg(VolumioMediaDto v) {
        Gson gson = new Gson();
        log.info("produceAndSendMsg={}", v);
        InputMessage inputMessage = InputMessage.builder()
                .event(ImportMessageEvent.IMPORT_VOLUMIO.toString())
                .data(gson.toJson(v))
                .build();
        inputMessageTemplate.send(topic, inputMessage);
    }

}
