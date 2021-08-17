package com.apa.importer;

import com.apa.importer.config.MusicCentralImporterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@SpringBootApplication
@Import(MusicCentralImporterConfiguration.class)
public class ImporterSpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImporterSpringBootTestApplication.class, args);
    }

    @Bean
    EmbeddedKafkaBroker broker() {
        return new EmbeddedKafkaBroker(1)
                .kafkaPorts(9092)
                .brokerListProperty("spring.kafka.bootstrap-servers");
    }
}
