package com.apa.events;

import com.apa.common.config.MusicCentralServicesConfiguration;
import com.apa.events.config.MusicCentralEventsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        MusicCentralServicesConfiguration.class,
        MusicCentralEventsConfiguration.class
})
public class EventsSpringBootTestApplication {
    public static void main(String ... args) {
        SpringApplication.run(EventsSpringBootTestApplication.class, args);
    }

}
