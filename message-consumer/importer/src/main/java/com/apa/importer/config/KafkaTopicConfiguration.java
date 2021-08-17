package com.apa.importer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic tidalImporterTopic() {
        return TopicBuilder
                .name("tidal.media.importer")
                .partitions(1)
                .replicas(1).build();
    }

    @Bean
    public NewTopic plexImporterTopic() {
        return TopicBuilder
                .name("plex.media.importer")
                .partitions(1)
                .replicas(1).build();
    }

    @Bean
    public NewTopic localImporterTopic() {
        return TopicBuilder
                .name("local.media.importer")
                .partitions(1)
                .replicas(1).build();
    }
}
