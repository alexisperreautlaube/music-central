package com.apa.consumer.config;

import com.apa.core.dto.media.LocalMediaDto;
import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.TidalMediaDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    public ConsumerFactory<String, LocalMediaDto> localMediaDtoConsumerFactory() {
        Map<String, Object> props = initCommonProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "media.importer");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(LocalMediaDto.class));
    }

    public ConsumerFactory<String, PlexMediaDto> plexMediaDtoConsumerFactory() {
        Map<String, Object> props = initCommonProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "media.importer");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PlexMediaDto.class));
    }

    public ConsumerFactory<String, TidalMediaDto> tidalMediaDtoConsumerFactory() {
        Map<String, Object> props = initCommonProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "media.importer");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(TidalMediaDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LocalMediaDto> localMediaDtoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LocalMediaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(localMediaDtoConsumerFactory());
        setFactoryCommonProperties(factory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PlexMediaDto> plexMediaDtoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PlexMediaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(plexMediaDtoConsumerFactory());
        setFactoryCommonProperties(factory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TidalMediaDto> tidalMediaDtoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TidalMediaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tidalMediaDtoConsumerFactory());
        setFactoryCommonProperties(factory);
        return factory;
    }

    @NotNull
    private Map<String, Object> initCommonProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        return props;
    }

    private void setFactoryCommonProperties(ConcurrentKafkaListenerContainerFactory<String, ?> factory) {
        factory.setErrorHandler(new SeekToCurrentErrorHandler());
    }
}
