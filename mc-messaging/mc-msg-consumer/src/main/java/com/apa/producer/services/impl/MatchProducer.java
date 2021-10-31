package com.apa.producer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.ProducedMatch;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.match.MatchMessageEvent;
import com.apa.common.msg.producer.MediaMediaData;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.common.services.util.ProduceMatchService;
import com.apa.events.mapper.PlexMediaMapper;
import com.apa.events.mapper.TidalMediaMapper;
import com.apa.events.mapper.VolumioMediaMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchProducer {

    @Value("${spring.kafka.topic.match.message}")
    private String topic;

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private KafkaTemplate<String, InputMessage> inputMessageTemplate;

    @Autowired
    private ProduceMatchService produceMatchService;

    public void producePlexToPlex() {
        Gson gson = new Gson();
        List<PlexMedia> all = plexMediaService.findAll();
        all.stream().forEach(
                from -> all.stream()
                        .filter(to -> !to.getPlexId().equals(from.getPlexId()))
                        .filter(to -> from.getTrackIndex() != null
                                && from.getTrackIndex().equals(to.getTrackIndex()))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                        .from(MediaReference.builder()
                                                .id(from.getPlexId())
                                                .clazz(from.getClass().getName())
                                                .build())
                                        .to(MediaReference.builder()
                                                .id(to.getPlexId())
                                                .clazz(to.getClass().getName())
                                                .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_PLEX_PLEX.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                                    .from(PlexMediaMapper.toPLexDto(from))
                                                    .to(PlexMediaMapper.toPLexDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getPlexId())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getPlexId())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }
    public void producePlexToTidal() {
        Gson gson = new Gson();
        List<PlexMedia> froms = plexMediaService.findAll();
        List<TidalMedia> tos = tidalMediaService.findAll();
        froms.stream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackIndex() != null
                                && from.getTrackIndex().equals(String.valueOf(to.getTrackNumber())))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getPlexId())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getTidalTrackId())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_PLEX_TIDAL.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                                    .from(PlexMediaMapper.toPLexDto(from))
                                                    .to(TidalMediaMapper.toTidalMediaDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getPlexId())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getTidalTrackId())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void producePlexToVolumio() {
        Gson gson = new Gson();
        List<PlexMedia> froms = plexMediaService.findAll();
        List<VolumioMedia> tos = volumioMediaService.findAll();
        froms.stream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackIndex() != null
                                && from.getTrackIndex().equals(to.getTrackNumber()))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getPlexId())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getTrackUri())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_PLEX_VOLUMIO.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                                    .from(PlexMediaMapper.toPLexDto(from))
                                                    .to(VolumioMediaMapper.toVolumioMediaDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getPlexId())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getTrackUri())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void produceTidalToPlex() {
        Gson gson = new Gson();
        List<TidalMedia> froms = tidalMediaService.findAll();
        List<PlexMedia> tos = plexMediaService.findAll();
        froms.stream().forEach(
                from -> tos.stream()
                        .filter(to -> String.valueOf(from.getTrackNumber()).equals(to.getTrackIndex()))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getTidalTrackId())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getPlexId())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_TIDAL_PLEX.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                            .from(TidalMediaMapper.toTidalMediaDto(from))
                                            .to(PlexMediaMapper.toPLexDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getTidalTrackId())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getPlexId())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void produceTidalToTidal() {
        Gson gson = new Gson();
        List<TidalMedia> froms = tidalMediaService.findAll();

        froms.stream().forEach(
                from -> froms.stream()
                        .filter(to -> !from.getTidalTrackId().equals(to.getTidalTrackId()))
                        .filter(to -> from.getTrackNumber() == to.getTrackNumber())
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getTidalTrackId())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getTidalTrackId())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_TIDAL_TIDAL.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                                    .from(TidalMediaMapper.toTidalMediaDto(from))
                                                    .to(TidalMediaMapper.toTidalMediaDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getTidalTrackId())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getTidalTrackId())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void produceTidalToVolumio() {
        Gson gson = new Gson();
        List<TidalMedia> froms = tidalMediaService.findAll();
        List<VolumioMedia> tos = volumioMediaService.findAll();
        froms.stream().forEach(
                from -> tos.stream()
                        .filter(to -> String.valueOf(from.getTrackNumber()).equals(to.getTrackNumber()))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getTidalTrackId())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getTrackUri())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_TIDAL_VOLUMIO.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                            .from(TidalMediaMapper.toTidalMediaDto(from))
                                            .to(VolumioMediaMapper.toVolumioMediaDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getTidalTrackId())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getTrackUri())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void produceVolumioToPlex() {
        Gson gson = new Gson();
        List<VolumioMedia> froms = volumioMediaService.findAll();
        List<PlexMedia> tos = plexMediaService.findAll();
        froms.stream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackNumber() != null
                                && from.getTrackNumber().equals(to.getTrackIndex()))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getTrackUri())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getPlexId())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_VOLUMIO_PLEX.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                            .from(VolumioMediaMapper.toVolumioMediaDto(from))
                                            .to(PlexMediaMapper.toPLexDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getTrackUri())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getPlexId())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void produceVolumioToTidal() {
        Gson gson = new Gson();
        List<VolumioMedia> froms = volumioMediaService.findAll();
        List<TidalMedia> tos = tidalMediaService.findAll();
        froms.stream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackNumber() != null
                                && from.getTrackNumber().equals(String.valueOf(to.getTrackNumber())))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getTrackUri())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getTidalTrackId())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_VOLUMIO_TIDAL.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                            .from(VolumioMediaMapper.toVolumioMediaDto(from))
                                            .to(TidalMediaMapper.toTidalMediaDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getTrackUri())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getTidalTrackId())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }

    public void produceVolumioToVolumio() {
        Gson gson = new Gson();
        List<VolumioMedia> froms = volumioMediaService.findAll();
        froms.stream().forEach(
                from -> froms.stream()
                        .filter(to -> from.getTrackNumber() != null
                                && from.getTrackNumber().equals(to.getTrackNumber()))
                        .filter(to -> !from.getTrackUri().equals(to.getTrackUri()))
                        .filter(to -> !produceMatchService.exist(ProducedMatch.builder()
                                .from(MediaReference.builder()
                                        .id(from.getTrackUri())
                                        .clazz(from.getClass().getName())
                                        .build())
                                .to(MediaReference.builder()
                                        .id(to.getTrackUri())
                                        .clazz(to.getClass().getName())
                                        .build())
                                .build()))
                        .forEach(to -> {
                            inputMessageTemplate.send(topic, InputMessage.builder()
                                    .event(MatchMessageEvent.MATCH_LEV_VOLUMIO_VOLUMIO.toString())
                                    .data(gson.toJson(MediaMediaData.builder()
                                                    .from(VolumioMediaMapper.toVolumioMediaDto(from))
                                                    .to(VolumioMediaMapper.toVolumioMediaDto(to))
                                            .build()))
                                    .build());
                            produceMatchService.save(ProducedMatch.builder()
                                    .from(MediaReference.builder()
                                            .id(from.getTrackUri())
                                            .clazz(from.getClass().getName())
                                            .build())
                                    .to(MediaReference.builder()
                                            .id(to.getTrackUri())
                                            .clazz(to.getClass().getName())
                                            .build())
                                    .build());
                        })
        );
    }
}
