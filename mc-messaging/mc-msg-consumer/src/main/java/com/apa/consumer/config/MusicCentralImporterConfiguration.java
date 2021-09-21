package com.apa.consumer.config;


import com.apa.events.config.MusicCentralEventsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MusicCentralEventsConfiguration.class,
        KafkaTopicConfiguration.class,
        KafkaProducerConfiguration.class,
        KafkaConsumerConfiguration.class
})
@ComponentScan(basePackages = "com.apa.consumer.services")
public class MusicCentralImporterConfiguration {
}
