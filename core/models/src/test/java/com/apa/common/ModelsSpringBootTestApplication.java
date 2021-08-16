package com.apa.common;

import com.apa.common.config.MusicCentralModelsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MusicCentralModelsConfiguration.class)
public class ModelsSpringBootTestApplication {
    public static void main(String ... args) {
        SpringApplication.run(ModelsSpringBootTestApplication.class, args);
    }
}

