package com.apa.controller;

import com.apa.client.volumio.VolumioClient;
import com.apa.common.services.media.AvailableMediasService;
import com.apa.producer.services.impl.MatchProducer;
import com.apa.producer.services.impl.PerfectMatchProducer;
import com.apa.producer.services.impl.VolumioNewSongImportProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.PathParam;

@RestController
@RequestMapping("/mc")
public class ConsumerTriggerController {

    @Autowired
    private PerfectMatchProducer perfectMatchProducer;

    @Autowired
    private MatchProducer matchProducer;

    @Autowired
    private AvailableMediasService availableMediasService;

    @Autowired
    private VolumioClient volumioClient;

    @Autowired
    private VolumioNewSongImportProducer volumioNewSongImportProducer;

    @GetMapping(value = "/PerfectMatch")
    public void producePlexPerfectMatchMessage() {
        perfectMatchProducer.producePlexPerfectMatchMessage();
        perfectMatchProducer.produceTidalPerfectMatchMessage();
        perfectMatchProducer.produceVolumioPerfectMatchMessage();
    }

    @GetMapping(value = "/PlexPlexMatch")
    public void producePlexPlexMatchMessage() {
        matchProducer.producePlexToPlex();
    }

    @GetMapping(value = "/PlexTidalMatch")
    public void producePlexTidalMatchMessage() {
        matchProducer.producePlexToTidal();
    }

    @GetMapping(value = "/PlexVolumioMatch")
    public void producePlexVolumioMatchMessage() {
        matchProducer.producePlexToVolumio();
    }

    @GetMapping(value = "/TidalPlexMatch")
    public void produceTidalPlexMatchMessage() {
        matchProducer.produceTidalToPlex();
    }

    @GetMapping(value = "/TidalTidalMatch")
    public void produceTidalTidalMatchMessage() {
        matchProducer.produceTidalToTidal();
    }

    @GetMapping(value = "/TidalVolumioMatch")
    public void produceTidalVolumioMatchMessage() {
        matchProducer.produceTidalToVolumio();
    }

    @GetMapping(value = "/VolumioPlexMatch")
    public void produceVolumioPlexMatchMessage() {
        matchProducer.produceVolumioToPlex();
    }

    @GetMapping(value = "/VolumioTidalMatch")
    public void produceVolumioTidalMatchMessage() {
        matchProducer.produceVolumioToTidal();
    }

    @GetMapping(value = "/VolumioVolumioMatch")
    public void produceVolumioVolumioMatchMessage() {
        matchProducer.produceVolumioToVolumio();
    }

    @GetMapping(value = "/allMediaMatch")
    public void allMediaMatch() {
        matchProducer.produceVolumioToVolumio();
        matchProducer.producePlexToPlex();
        matchProducer.producePlexToTidal();
        matchProducer.producePlexToVolumio();
        matchProducer.produceTidalToPlex();
        matchProducer.produceTidalToTidal();
        matchProducer.produceTidalToVolumio();
        matchProducer.produceVolumioToPlex();
        matchProducer.produceVolumioToTidal();
        matchProducer.produceVolumioToVolumio();
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
