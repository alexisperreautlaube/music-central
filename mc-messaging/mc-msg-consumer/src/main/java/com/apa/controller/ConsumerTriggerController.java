package com.apa.controller;

import com.apa.client.volumio.VolumioClient;
import com.apa.common.services.media.AvailableMediasService;
import com.apa.producer.services.impl.MatchProducer;
import com.apa.producer.services.impl.VolumioNewSongImportProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.PathParam;

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
        log.info("VolumioTidalMatch start");
    }

    @GetMapping(value = "/VolumioVolumioMatch")
    public void produceVolumioVolumioMatchMessage() {
        log.info("VolumioVolumioMatch start");
        matchProducer.produceVolumioToVolumio();
        log.info("VolumioVolumioMatch start");
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
        availableMediasService.createAvailableList();
    }

    @GetMapping(value = "/refreshQueue")
    public void refreshQueue() {
        volumioClient.refreshQueue();
    }

    @GetMapping(value = "/produceNewVolumioTrackMessage")
    public void produceNewVolumioTrackMessage() {
        volumioNewSongImportProducer.produceNewVolumioTrackMessage();
    }

    @GetMapping(value = "/rating/{uri}")
    public Integer rating(@PathParam("uri") String uri) {
        return availableMediasService.getRating(uri);
    }
}
