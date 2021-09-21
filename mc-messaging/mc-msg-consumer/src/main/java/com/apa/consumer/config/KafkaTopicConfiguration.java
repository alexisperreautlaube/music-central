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

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Value("${spring.kafka.topic.local.media.importer}")
    private String localTopic;

    @Value("${spring.kafka.topic.plex.media.importer}")
    private String plexTopic;

    @Value("${spring.kafka.topic.tidal.media.importer}")
    private String tidalTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic tidalImporterTopic() {
        return TopicBuilder
                .name(tidalTopic)
                .partitions(1)
                .replicas(1).build();
    }

    @Bean
    public NewTopic plexImporterTopic() {
        return TopicBuilder
                .name(plexTopic)
                .partitions(1)
                .replicas(1).build();
    }

    @Bean
    public NewTopic localImporterTopic() {
        return TopicBuilder
                .name(localTopic)
                .partitions(1)
                .replicas(1).build();
    }
}
