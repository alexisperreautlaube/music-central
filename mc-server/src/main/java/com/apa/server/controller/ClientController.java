package com.apa.server.controller;


import com.apa.client.apple.AppleClient;
import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.service.AppleAvailableTrackService;
import com.apa.client.apple.service.AppleEqService;
import com.apa.client.apple.service.AppleTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mc/client")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private AppleTrackService appleTrackService;

    @Autowired
    private AppleAvailableTrackService appleAvailableTrackService;

    @Autowired
    private AppleEqService appleEqService;

    @Autowired
    private AppleClient client;

    @GetMapping(value = "/logCurrentTrack")
    public void logCurrentTrack() {
        client.getCurrentTrack();
    }

    @GetMapping(value = "/doItAll")
    public void doItAll() {
        createAvailableTracks();
        createTriageList();
        createBestOf();
    }

    @GetMapping(value = "/refreshAvailableTracks")
    public void createAvailableTracks() {
        log.info("start createAvailableTracks");
        appleAvailableTrackService.refreshAppleAvailableTrack();
        appleAvailableTrackService.refreshWeight();
        log.info("end createAvailableTracks");

    }

    @GetMapping(value = "/createTriageList")
    public void createTriageList() {
        log.info("start createTriageList");
        List<Long> triageList = appleAvailableTrackService.createTriageList();
        client.emptyTriageList();
        client.fillTriageList(triageList);
        log.info("end createTriageList");
    }

    @GetMapping(value = "/createBestOf")
    public void createBestOf() {
        log.info("start createBestOf");
        List<Long> bestOf = appleAvailableTrackService.createBestOf();
        client.emptyBestOf();
        client.fillBestOf(bestOf);
        log.info("end createBestOf");
    }

    @GetMapping(value = "/playPause")
    public void playPause() {
        client.playPause();
    }

    @GetMapping(value = "/search")
    public void search() {
        client.search();
    }


    @GetMapping(value = "/getCurrentSong")
    public AppleTrack getCurrentSong() {
        AppleTrack currentTrack = client.getCurrentTrack();
        appleEqService.setEqualizer(currentTrack);
        return currentTrack;
    }
}
