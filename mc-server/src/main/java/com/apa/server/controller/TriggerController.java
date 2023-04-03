package com.apa.server.controller;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.service.AppleAvailableTrackService;
import com.apa.client.apple.service.AppleTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/mc/trigger")
@CrossOrigin(origins = "http://localhost:3000")
public class TriggerController {

    @Autowired
    private AppleTrackService appleTrackService;

    @Autowired
    private AppleClient appleClient;
    
    @Autowired
    private AppleAvailableTrackService appleAvailableTrackService;
    
    @GetMapping(value = "/doAll")
    public void doAll() {
        log.info("doAll start");
        saveAllTrackByList();
        refreshAppleAvailableTrack();
        refreshWeight();
        createTriageList();
        createBestOf();
        log.info("doAll end");
    }

    @GetMapping(value = "/saveAllTrackByList")
    public void saveAllTrackByList() {
        log.info("saveAllTrackByList start");
        appleTrackService.saveAllTracksByList();
        log.info("saveAllTrackByList end");
    }

    @GetMapping(value = "/refreshAppleAvailableTrack")
    public void refreshAppleAvailableTrack() {
        log.info("refreshAppleAvailableTrack start");
        appleAvailableTrackService.refreshAppleAvailableTrack();
        log.info("refreshAppleAvailableTrack end");
    }
    @GetMapping(value = "/createTriageList")
    public void createTriageList() {
        log.info("createTriageList start");
        List<Long> triageList = appleAvailableTrackService.createTriageList();
        appleClient.emptyTriageList();
        appleClient.fillTriageList(triageList);
        log.info("createTriageList end");
    }

    @GetMapping(value = "/refreshWeight")
    public void refreshWeight() {
        log.info("refreshWeight start");
        appleAvailableTrackService.refreshWeight();
        log.info("refreshWeight end");
    }

    @GetMapping(value = "/createBestOf")
    public void createBestOf() {
        log.info("createBestOf start");
        List<Long> bestOf = appleAvailableTrackService.createBestOf();
        appleClient.emptyBestOf();
        appleClient.fillBestOf(bestOf);
        log.info("createBestOf end");
    }
    @GetMapping(value = "/playPause")
    public void playPause() {
        log.info("playPause start");
        appleClient.playPause();
        log.info("playPause end");
    }
}
