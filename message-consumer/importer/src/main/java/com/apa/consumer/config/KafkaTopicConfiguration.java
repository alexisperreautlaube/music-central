package com.apa.consumer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }

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
