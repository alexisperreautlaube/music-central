package com.apa.server.controller;

import com.apa.client.volumio.VolumioClient;
import com.apa.common.entities.enums.RatingType;
import com.apa.common.entities.media.Rating;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.mapper.VolumioMediaMapper;
import com.apa.common.services.match.MatchProducer;
import com.apa.common.services.media.AvailableMediasService;
import com.apa.common.services.media.impl.RatingService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.common.services.newsongs.VolumioNewSongImportProducer;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.executor.impl.VolumioMediaImporter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/mc")
public class ConsumerTriggerController {

    @Autowired
    private MatchProducer matchProducer;

    @Autowired
    private AvailableMediasService availableMediasService;

    @Autowired
    private VolumioClient volumioClient;

    @Autowired
    private VolumioNewSongImportProducer volumioNewSongImportProducer;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private VolumioMediaImporter volumioMediaImporter;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @GetMapping(value = "/PlexPlexMatch")
    public void producePlexPlexMatchMessage() {
        matchProducer.producePlexToPlex();
    }

    @GetMapping(value = "/TidalPlexMatch")
    public void produceTidalPlexMatchMessage() {
        matchProducer.produceTidalToPlex();
    }

    @GetMapping(value = "/TidalTidalMatch")
    public void produceTidalTidalMatchMessage() {
        matchProducer.produceTidalToTidal();
    }

    @GetMapping(value = "/VolumioPlexMatch")
    public void produceVolumioPlexMatchMessage() {
        log.info("VolumioPlexMatch start");
        matchProducer.produceVolumioToPlex();
        log.info("VolumioPlexMatch end");
    }

    @GetMapping(value = "/VolumioTidalMatch")
    public void produceVolumioTidalMatchMessage() {
        log.info("VolumioTidalMatch start");
        matchProducer.produceVolumioToTidal();
        log.info("VolumioTidalMatch end");
    }

    @GetMapping(value = "/VolumioVolumioMatch")
    public void produceVolumioVolumioMatchMessage() {
        log.info("VolumioVolumioMatch start");
        matchProducer.produceVolumioToVolumio();
        log.info("VolumioVolumioMatch end");
    }

    @GetMapping(value = "/allMediaMatch")
    public void allMediaMatch() {
        log.info("allMediaMatch start");
        matchProducer.producePlexToPlex();
        matchProducer.produceTidalToPlex();
        matchProducer.produceTidalToTidal();
        matchProducer.produceVolumioToPlex();
        matchProducer.produceVolumioToTidal();
        matchProducer.produceVolumioToVolumio();
        log.info("allMediaMatch end");
    }

    @GetMapping(value = "/createAvailableTrack")
    public void createAvailableTrack() {
        log.info("createAvailableTrack start");
        volumioMediaService.createAvailableList();
        log.info("createAvailableTrack end");
    }

    @GetMapping(value = "/refreshQueue")
    public void refreshQueue() {
        log.info("refreshQueue start");
        volumioClient.refreshQueue(volumioMediaService.getList());
        log.info("refreshQueue end");
    }

    @GetMapping(value = "/produceNewVolumioTrackMessage")
    public void produceNewVolumioTrackMessage() {
        log.info("produceNewVolumioTrackMessage start");
        List<VolumioMediaDto> volumioMediaDtos = volumioNewSongImportProducer.getNewVolumioTracks();
        volumioMediaDtos.parallelStream().forEach(v -> volumioMediaImporter.execute(v));
        log.info("produceNewVolumioTrackMessage end");
    }

    @GetMapping(value = "/nightly")
    public void nightly() {
        log.info("nightly start");
        List<VolumioMediaDto> volumioMediaDtos = volumioNewSongImportProducer.getNewVolumioTracks();
        volumioMediaDtos.parallelStream().forEach(v -> volumioMediaImporter.execute(v));
        matchProducer.produceVolumioToPlex(volumioMediaDtos);
        matchProducer.produceVolumioToTidal(volumioMediaDtos);
        matchProducer.produceVolumioToVolumio(volumioMediaDtos);
        availableMediasService.createAvailableList(volumioMediaDtos.parallelStream()
                .map(v -> VolumioMediaMapper.toVolumioMedia(v))
                .collect(Collectors.toList()), volumioMediaService::doFindById);
        refreshQueue();
        log.info("nightly end");
    }

    @GetMapping(value = "/weekly")
    public void weekly() {
        log.info("weekly start");
        produceNewVolumioTrackMessage();
        allMediaMatch();
        createAvailableTrack();
        refreshQueue();
        log.info("weekly end");
    }

    @Getter
    @Setter
    private class RatingBody {
        private String uri;
    }

    @GetMapping(value = "/rating1/")
    public Integer rating1() {
        String uri = volumioClient.getCurrentPlayedUri1();
        return availableMediasService.getRating(uri);
    }

    @GetMapping(value = "/rating2/")
    public Integer rating2() {
        String uri = volumioClient.getCurrentPlayedUri1();
        return availableMediasService.getRating(uri);
    }

    @PostMapping(value = "/ratePlaying1/{rating}")
    public void ratePlaying1(@PathVariable("rating") Integer rating) {
        String uri = volumioClient.getCurrentPlayedUri1();
        ratingService.save(Rating.builder()
                        .rating(rating)
                        .mediaReference(MediaReference.builder()
                                .clazz(VolumioMedia.class.getName())
                                .id(uri)
                                .build())
                        .ratingType(RatingType.VOLUMIO_MANUAL)
                        .rateDate(LocalDateTime.now())
                .build());
    }

    @PostMapping(value = "/ratePlaying2/{rating}")
    public void ratePlaying2(@PathVariable("rating") Integer rating) {
        String uri = volumioClient.getCurrentPlayedUri2();
        ratingService.save(Rating.builder()
                        .rating(rating)
                        .mediaReference(MediaReference.builder()
                                .clazz(VolumioMedia.class.getName())
                                .id(uri)
                                .build())
                        .ratingType(RatingType.VOLUMIO_MANUAL)
                        .rateDate(LocalDateTime.now())
                .build());
    }

    @GetMapping(value = "/syncDbTidalReleaseDate")
    public void syncDbTidalReleaseDate() {
        volumioMediaService.syncTidalReleaseDate();
    }

    @GetMapping(value = "/syncTrackArtistAndAlbumArtist")
    public void syncTrackArtistAndAlbumArtist() {
        volumioMediaService.syncTrackArtistAndAlbumArtist();
    }

    @GetMapping(value = "/syncPlexDate")
    public void syncPlexDate() {
        volumioMediaService.syncPlexDate();
    }

    @GetMapping(value = "/syncTidalDateFromFile")
    public void syncTidalDateFromFile() {
        volumioMediaService.syncTidalDateFromFile();
    }
}
