package com.apa.importer.config;


import com.apa.events.config.MusicCentralEventsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MusicCentralEventsConfiguration.class,
        KafkaTopicConfiguration.class,
        KafkaProducerConfiguration.class,
        KafkaConsumerConfiguration.class
})
public class MusicCentralImporterConfiguration {
}
