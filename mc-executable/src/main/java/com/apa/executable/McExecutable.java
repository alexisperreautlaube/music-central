package com.apa.executable;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.service.AppleAvailableTrackService;
import com.apa.client.apple.service.AppleTrackService;
import com.apa.executable.config.McExecutableConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.List;

@Slf4j
@SpringBootApplication
@Import(McExecutableConfiguration.class)
public class McExecutable {

    public static void main(String ... args) {
        ConfigurableApplicationContext context = SpringApplication.run(McExecutable.class, args);
        AppleClient client = context.getBean(AppleClient.class);
        AppleAvailableTrackService appleAvailableTrackService = context.getBean(AppleAvailableTrackService.class);
        AppleTrackService appleTrackService = context.getBean(AppleTrackService.class);

        log.info("start saveAllTracksByList");
        appleTrackService.saveAllTracksByList();
        log.info("end saveAllTracksByList");

        log.info("start createAvailableTracks");
        appleAvailableTrackService.refreshAppleAvailableTrack();
        log.info("end createAvailableTracks");

        log.info("start refreshWeight");
        appleAvailableTrackService.refreshWeight();
        log.info("end refreshWeight");

        log.info("start createTriageList");
        List<Long> triageList = appleAvailableTrackService.createTriageList();
        client.emptyTriageList();
        client.fillTriageList(triageList);
        log.info("end createTriageList");

        log.info("start createBestOf");
        List<Long> bestOf = appleAvailableTrackService.createBestOf();
        client.emptyBestOf();
        client.fillBestOf(bestOf);
        log.info("end createBestOf");
    }
}
