package com.apa.server.controller;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.entity.Equalizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/mc/")
@CrossOrigin(origins = "http://localhost:3000")
public class EqualizerController {

    @Autowired
    private AppleClient appleClient;


    @PostMapping(value = "/eq/{equalizer}")
    public void setEqualizer(@PathVariable("equalizer") String equalizer) {
        log.info("equalizer={}", equalizer);
        appleClient.setEqualizer(equalizer);
    }

    @PostMapping(value = "/eq")
    public void updateEqualizer(@RequestBody Equalizer equalizer) {
        log.info("equalizer={}", equalizer);
    }

}
