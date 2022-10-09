package com.apa.producer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.services.media.impl.plex.PlexMediaDistanceService;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaDistanceService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaDistanceService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.mapper.VolumioMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchProducer {

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    private TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    private VolumioMediaDistanceService volumioMediaDistanceService;

    public void producePlexToPlex() {
        List<PlexMedia> all = plexMediaService.findAll();
        all.parallelStream().forEach(
                from -> all.stream()
                        .filter(to -> !to.getPlexId().equals(from.getPlexId()))
                        .filter(to -> from.getTrackIndex() != null
                                && from.getTrackIndex().equals(to.getTrackIndex()))
                        .forEach(to -> {
                            plexMediaDistanceService.distance(from, to);
                        })
        );
    }
    public void producePlexToTidal() {
        List<PlexMedia> froms = plexMediaService.findAll();
        List<TidalMedia> tos = tidalMediaService.findAll();
        froms.parallelStream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackIndex() != null
                                && from.getTrackIndex().equals(String.valueOf(to.getTrackNumber())))
                        .forEach(to -> {
                            plexMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void producePlexToVolumio() {
        List<PlexMedia> froms = plexMediaService.findAll();
        List<VolumioMedia> tos = volumioMediaService.findAll();
        froms.parallelStream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackIndex() != null
                                && from.getTrackIndex().equals(to.getTrackNumber()))
                        .forEach(to -> {
                            plexMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void produceTidalToPlex() {
        List<TidalMedia> froms = tidalMediaService.findAll();
        List<PlexMedia> tos = plexMediaService.findAll();
        froms.parallelStream().forEach(
                from -> tos.stream()
                        .filter(to -> String.valueOf(from.getTrackNumber()).equals(to.getTrackIndex()))
                        .forEach(to -> {
                            tidalMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void produceTidalToTidal() {
        List<TidalMedia> froms = tidalMediaService.findAll();
        froms.parallelStream().forEach(
                from -> froms.stream()
                        .filter(to -> !from.getTidalTrackId().equals(to.getTidalTrackId()))
                        .filter(to -> from.getTrackNumber() == to.getTrackNumber())
                        .forEach(to -> {
                            tidalMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void produceTidalToVolumio() {
        List<TidalMedia> froms = tidalMediaService.findAll();
        List<VolumioMedia> tos = volumioMediaService.findAll();
        froms.parallelStream().forEach(
                from -> tos.stream()
                        .filter(to -> String.valueOf(from.getTrackNumber()).equals(to.getTrackNumber()))
                        .forEach(to -> {
                            tidalMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void produceVolumioToPlex() {
        List<VolumioMedia> froms = volumioMediaService.findAll();
        privateProduceVolumioToPlex(froms);
    }

    public void produceVolumioToPlex(List<VolumioMediaDto> volumioMediaDtos) {
        privateProduceVolumioToPlex(volumioMediaDtos.parallelStream().map(v -> VolumioMediaMapper.toVolumioMedia(v)).collect(Collectors.toList()));
    }

    private void privateProduceVolumioToPlex(List<VolumioMedia> froms) {
        List<PlexMedia> tos = plexMediaService.findAll();
        froms.parallelStream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackNumber() != null
                                && from.getTrackNumber().equals(to.getTrackIndex()))
                        .forEach(to -> {
                            volumioMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void produceVolumioToTidal() {
        List<VolumioMedia> froms = volumioMediaService.findAll();
        privateProduceVolumioToTidal(froms);
    }

    public void produceVolumioToTidal(List<VolumioMediaDto> volumioMediaDtos) {
        privateProduceVolumioToTidal(volumioMediaDtos.parallelStream().map(v -> VolumioMediaMapper.toVolumioMedia(v)).collect(Collectors.toList()));
    }

    private void privateProduceVolumioToTidal(List<VolumioMedia> froms) {
        List<TidalMedia> tos = tidalMediaService.findAll();
        froms.parallelStream().forEach(
                from -> tos.stream()
                        .filter(to -> from.getTrackNumber() != null
                                && from.getTrackNumber().equals(String.valueOf(to.getTrackNumber())))
                        .forEach(to -> {
                            volumioMediaDistanceService.distance(from, to);
                        })
        );
    }

    public void produceVolumioToVolumio() {
        List<VolumioMedia> froms = volumioMediaService.findAll();
        privateProduceVolumioToVolumio(froms);
    }
    public void produceVolumioToVolumio(List<VolumioMediaDto> volumioMediaDtos) {
        privateProduceVolumioToVolumio(volumioMediaDtos.parallelStream().map(v -> VolumioMediaMapper.toVolumioMedia(v)).collect(Collectors.toList()));
    }
    private void privateProduceVolumioToVolumio(List<VolumioMedia> froms) {
        froms.parallelStream().forEach(
                from -> froms.stream()
                        .filter(to -> from.getTrackNumber() != null
                                && from.getTrackNumber().equals(to.getTrackNumber()))
                        .filter(to -> !from.getTrackUri().equals(to.getTrackUri()))
                        .forEach(to -> {
                            volumioMediaDistanceService.distance(from, to);
                        })
        );
    }
}
