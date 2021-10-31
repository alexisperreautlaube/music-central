package com.apa.producer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.match.MatchMessageEvent;
import com.apa.common.services.media.impl.plex.PlexMediaDistanceService;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaDistanceService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaDistanceService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.events.mapper.PlexMediaMapper;
import com.apa.events.mapper.TidalMediaMapper;
import com.apa.events.mapper.VolumioMediaMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PerfectMatchProducer {

    @Value("${spring.kafka.topic.match.message}")
    private String topic;

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    VolumioMediaDistanceService volumioMediaDistanceService;

    @Autowired
    private KafkaTemplate<String, InputMessage> inputMessageTemplate;

    public void producePlexPerfectMatchMessage() {
        log.info("producePlexPerfectMatchMessage start");
        Gson gson = new Gson();
        List<PlexMedia> all = plexMediaService.findAll();
        all.stream()
                .forEach(p -> inputMessageTemplate.send(topic, InputMessage.builder()
                                .event(MatchMessageEvent.MATCH_PLEX_PERFECT.toString())
                                .data(gson.toJson(PlexMediaMapper.toPLexDto(p)))
                        .build()));
        log.info("producePlexPerfectMatchMessage end");
    }

    public void produceTidalPerfectMatchMessage() {
        Gson gson = new Gson();
        List<TidalMedia> all = tidalMediaService.findAll();
        all.stream()
                .forEach(p -> inputMessageTemplate.send(topic, InputMessage.builder()
                        .event(MatchMessageEvent.MATCH_TIDAL_PERFECT.toString())
                        .data(gson.toJson(TidalMediaMapper.toTidalMediaDto(p)))
                        .build()));
    }

    public void produceVolumioPerfectMatchMessage() {
        Gson gson = new Gson();
        List<VolumioMedia> all = volumioMediaService.findAll();
        all.stream()
                .forEach(p -> inputMessageTemplate.send(topic, InputMessage.builder()
                        .event(MatchMessageEvent.MATCH_VOLUMIO_PERFECT.toString())
                        .data(gson.toJson(VolumioMediaMapper.toVolumioMediaDto(p)))
                        .build()));
    }
}
