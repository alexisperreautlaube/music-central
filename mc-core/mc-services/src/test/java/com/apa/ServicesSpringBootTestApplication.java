package com.apa;

import com.apa.common.config.MusicCentralServicesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MusicCentralServicesConfiguration.class)
public class ServicesSpringBootTestApplication {
    public static void main(String ... args) {
        SpringApplication.run(ServicesSpringBootTestApplication.class, args);
    }
}

