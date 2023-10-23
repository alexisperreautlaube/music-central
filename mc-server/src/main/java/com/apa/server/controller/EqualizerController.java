package com.apa.server.controller;


import com.apa.client.apple.entity.AppleEqualizerAndBands;
import com.apa.common.entities.equalizer.entity.Equalizer;
import com.apa.client.apple.service.AppleEqService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/mc/")
@CrossOrigin(origins = "http://localhost:3000")
public class EqualizerController {

    @Autowired
    private AppleEqService appleEqService;

    @GetMapping(value = "eq/refresh")
    public void updateEqualizer() {
        appleEqService.getAndSaveAllEQ();
    }


    @GetMapping(value = "eq/current")
    public AppleEqualizerAndBands getCurrentEqualizer() {
        return appleEqService.getCurrentEq();
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
