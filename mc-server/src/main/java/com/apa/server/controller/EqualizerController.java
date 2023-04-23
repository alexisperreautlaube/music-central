package com.apa.server.controller;

import com.apa.client.apple.AppleClient;
import com.apa.common.entities.equalizer.entity.Equalizer;
import com.apa.service.NativeEqualizer;
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

    @Autowired
    private NativeEqualizer nativeEqualizer;

    @PostMapping(value = "/eq/{equalizer}")
    public void setEqualizer(@PathVariable("equalizer") String equalizer) {
        log.info("equalizer={}", equalizer);
        appleClient.setEqualizer(equalizer);
    }

    @PostMapping(value = "/eq")
    public void updateEqualizer(@RequestBody Equalizer equalizer) {
        log.info("equalizer={}", equalizer);
        float[] floats = {equalizer.getF32(),
                equalizer.getF64(),
                equalizer.getF125(),
                equalizer.getF250(),
                equalizer.getF500(),
                equalizer.getF1000(),
                equalizer.getF2000(),
                equalizer.getF4000(),
                equalizer.getF8000(),
                equalizer.getF16000()
        };
        nativeEqualizer.setEqualizer(floats);
    }

}
