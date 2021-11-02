package com.apa.producer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.ProducedMatch;
import com.apa.common.services.media.impl.plex.PlexMediaDistanceService;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaDistanceService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaDistanceService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.common.services.util.ProduceMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ProduceMatchService produceMatchService;

    @Autowired
    private PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    private TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    private VolumioMediaDistanceService volumioMediaDistanceService;

    public void producePlexToPlex() {
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
                            plexMediaDistanceService.distance(from, to);
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
                            plexMediaDistanceService.distance(from, to);
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
                            plexMediaDistanceService.distance(from, to);
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
                            tidalMediaDistanceService.distance(from, to);
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
                            tidalMediaDistanceService.distance(from, to);
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
                            tidalMediaDistanceService.distance(from, to);
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
                            volumioMediaDistanceService.distance(from, to);
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
                            volumioMediaDistanceService.distance(from, to);
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
                            volumioMediaDistanceService.distance(from, to);
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
