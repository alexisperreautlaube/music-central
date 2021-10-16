package com.apa.controller;

import com.apa.producer.services.impl.MatchProducer;
import com.apa.producer.services.impl.PerfectMatchProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mc")
public class ConsumerTriggerController {

    @Autowired
    private PerfectMatchProducer perfectMatchProducer;

    @Autowired
    private MatchProducer matchProducer;

    @GetMapping(value = "/PlexPerfectMatch")
    public void producePlexPerfectMatchMessage() {
        perfectMatchProducer.producePlexPerfectMatchMessage();
    }

    @GetMapping(value = "/TidalPerfectMatch")
    public void produceTidalPerfectMatchMessage() {
        perfectMatchProducer.produceTidalPerfectMatchMessage();
    }

    @GetMapping(value = "/VolumioPerfectMatch")
    public void produceVolumioPerfectMatchMessage() {
        perfectMatchProducer.produceVolumioPerfectMatchMessage();
    }

    @GetMapping(value = "/PlexPlextMatch")
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

    @GetMapping(value = "/TidalPlextMatch")
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

    @GetMapping(value = "/VolumioPlextMatch")
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

}
