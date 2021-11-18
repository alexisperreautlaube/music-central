package com.apa.producer.services.impl;

import com.apa.client.volumio.VolumioClient;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.impor.ImportMessageEvent;
import com.apa.core.dto.media.VolumioMediaDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class VolumioNewSongImportProducer {

    @Value("${spring.kafka.topic.import.message}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, InputMessage> inputMessageTemplate;

    @Autowired
    private VolumioClient volumioClient;

    public List<VolumioMediaDto> produceNewVolumioTrackMessage() {
        List<VolumioMediaDto> volumioMediaDtos = getNewVolumioTrack();
        volumioMediaDtos.parallelStream().forEach(v -> produceAndSendMsg(v));
        return volumioMediaDtos;
    }

    @NotNull
    private List<VolumioMediaDto> getNewVolumioTrack() {
        List<VolumioMediaDto> notSavedVolumioLocalAlbum = volumioClient.getNotSavedVolumioLocalAlbum();
        List<VolumioMediaDto> notSavedVolumioTidalAlbum = volumioClient.getNotSavedVolumioTidalAlbum();
        List<VolumioMediaDto> volumioMediaDtos = Stream.concat(notSavedVolumioLocalAlbum.stream(), notSavedVolumioTidalAlbum.stream()).collect(Collectors.toList());
        return volumioMediaDtos;
    }

    private void produceAndSendMsg(VolumioMediaDto v) {
        Gson gson = new Gson();
        InputMessage inputMessage = InputMessage.builder()
                .event(ImportMessageEvent.IMPORT_VOLUMIO.toString())
                .data(gson.toJson(v))
                .build();
        inputMessageTemplate.send(topic, inputMessage);
    }

}
