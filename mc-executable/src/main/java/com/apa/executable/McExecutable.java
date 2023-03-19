package com.apa.executable;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.service.AppleAvailableTrackService;
import com.apa.client.apple.service.AppleTrackService;
import com.apa.executable.config.McExecutableConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
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
        log.info("args={}", args);
        //saveAllTrackByList(appleTrackService);
        if (ArrayUtils.contains(args, "doAll")) {
            doAll(client, appleAvailableTrackService, appleTrackService);
        }
        if (ArrayUtils.contains(args, "saveAllTrackByList")) {
            saveAllTrackByList(appleTrackService);
        }
        if (ArrayUtils.contains(args, "refreshAppleAvailableTrack")) {
            refreshAppleAvailableTrack(appleAvailableTrackService);
        }
        if (ArrayUtils.contains(args, "refreshWeight")) {
            refreshWeight(appleAvailableTrackService);
        }
        if (ArrayUtils.contains(args, "createTriageList")) {
            createTriageList(client, appleAvailableTrackService);
        }
        if (ArrayUtils.contains(args, "createBestOf")) {
            createBestOf(client, appleAvailableTrackService);
        }

    }

    private static void doAll(AppleClient client, AppleAvailableTrackService appleAvailableTrackService, AppleTrackService appleTrackService) {
        saveAllTrackByList(appleTrackService);

        refreshAppleAvailableTrack(appleAvailableTrackService);

        refreshWeight(appleAvailableTrackService);

        createTriageList(client, appleAvailableTrackService);

        createBestOf(client, appleAvailableTrackService);
    }

    private static void createBestOf(AppleClient client, AppleAvailableTrackService appleAvailableTrackService) {
        log.info("start createBestOf");
        List<Long> bestOf = appleAvailableTrackService.createBestOf();
        client.emptyBestOf();
        client.fillBestOf(bestOf);
        log.info("end createBestOf");
    }

    private static void createTriageList(AppleClient client, AppleAvailableTrackService appleAvailableTrackService) {
        log.info("start createTriageList");
        List<Long> triageList = appleAvailableTrackService.createTriageList();
        client.emptyTriageList();
        client.fillTriageList(triageList);
        log.info("end createTriageList");
    }

    private static void refreshWeight(AppleAvailableTrackService appleAvailableTrackService) {
        log.info("start refreshWeight");
        appleAvailableTrackService.refreshWeight();
        log.info("end refreshWeight");
    }

    private static void refreshAppleAvailableTrack(AppleAvailableTrackService appleAvailableTrackService) {
        log.info("start refreshAppleAvailableTrack");
        appleAvailableTrackService.refreshAppleAvailableTrack();
        log.info("end refreshAppleAvailableTrack");
    }

    private static void saveAllTrackByList(AppleTrackService appleTrackService) {
        log.info("start saveAllTracksByList");
        appleTrackService.saveAllTracksByList();
        log.info("end saveAllTracksByList");
    }
}
