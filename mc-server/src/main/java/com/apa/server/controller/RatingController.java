package com.apa.server.controller;

import com.apa.client.apple.AppleClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/mc/")
@CrossOrigin(origins = "http://localhost:3000")
public class RatingController {

    @Autowired
    private AppleClient appleClient;

    @PostMapping(value = "/rate/{rating}")
    public void ratePlaying1(@PathVariable("rating") Integer rating) {
        log.info("rate={}", rating);
        appleClient.setRatingOfCurrentTrackAndSkip(rating * 20);
    }

}
