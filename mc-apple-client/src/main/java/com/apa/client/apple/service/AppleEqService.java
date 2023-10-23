package com.apa.client.apple.service;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.entity.AppleEqualizer;
import com.apa.client.apple.entity.AppleEqualizerAndBands;
import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.repository.AppleEqualizerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;



@Slf4j
@Component
public class AppleEqService {

    @Autowired
    private AppleEqualizerRepository appleEqualizerRepository;

    @Autowired
    private AppleClient appleClient;

    private AppleEqualizer cachedEq;

    public void getAndSaveAllEQ(){
        appleEqualizerRepository.deleteAll();
        List<AppleEqualizer> appleEqualizerList = appleClient.getAppleEqualizerList();
        appleEqualizerRepository.saveAll(appleEqualizerList);
    }

    private Optional<AppleEqualizer> getAppleEqualizerByName(String name) {
        return appleEqualizerRepository.findAppleEqualizerByName(name);
    }

    public AppleEqualizer getCachedEq() {
        if (cachedEq == null) {
            Optional<AppleEqualizer> appleEqualizerByName = appleEqualizerRepository.findAppleEqualizerByName(appleClient.getCurrentEq().getName());
            cachedEq = appleEqualizerByName.get();
        }
        return cachedEq;
    }

    public AppleEqualizerAndBands getCurrentEq() {
        return appleClient.getCurrentEq();
    }

    public void setEqualizer(AppleTrack appleTrack) {
        AppleEqualizer eq = getCachedEq();
        if (eq.getName().equals("_" + appleTrack.getArtist())) {
            return;
        }
        Optional<AppleEqualizer> appleEqualizerByName = getAppleEqualizerByName("_" + appleTrack.getArtist());
        eq = appleEqualizerByName.orElse(getAppleEqualizerByName("Flat").get());
        appleClient.setEqualizer(eq.getIndex().toString());
        cachedEq = eq;
    }
}
