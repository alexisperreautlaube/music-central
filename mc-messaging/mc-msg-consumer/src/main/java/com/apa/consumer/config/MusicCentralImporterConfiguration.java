package com.apa.consumer.config;


import com.apa.events.config.MusicCentralEventsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
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
    @Bean
    public ObjectMapper createOjbectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
