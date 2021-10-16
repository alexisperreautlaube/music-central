package com.apa.controller;

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
}
