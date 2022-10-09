package com.apa.importer;

import com.apa.consumer.config.MusicCentralImporterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MusicCentralImporterConfiguration.class})
public class ImporterSpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImporterSpringBootTestApplication.class, args);
    }

}
