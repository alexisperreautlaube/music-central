package com.apa.server.controller;

import com.apa.client.apple.service.AppleTrackService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/mc/trigger")
public class TriggerController {

    @Autowired
    private AppleTrackService appleTrackService;

    @GetMapping(value = "/createAvailableTrack")
    public void createAvailableTrack() {
        log.info("createAvailableTrack start");

        log.info("createAvailableTrack end");
    }

    @GetMapping(value = "/refreshQueue")
    public void refreshQueue() {
        log.info("refreshQueue start");

        log.info("refreshQueue end");
    }

    @GetMapping(value = "/nightly")
    public void nightly() {
        log.info("nightly start");
        log.info("nightly end");
    }

    @GetMapping(value = "/weekly")
    public void weekly() {
        log.info("weekly start");
        createAvailableTrack();
        refreshQueue();
        log.info("weekly end");
    }

    @Getter
    @Setter
    private class RatingBody {
        private String uri;
    }


}
