package com.apa.events;

import com.apa.common.config.MusicCentralConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MusicCentralConfig.class)
public class EventsSpringBootTestApplication {
    public static void main(String ... args) {
        SpringApplication.run(EventsSpringBootTestApplication.class, args);
    }

}
