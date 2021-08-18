package com.apa.consumer.config;

import com.apa.core.dto.media.LocalMediaDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {



    public ConsumerFactory<String, LocalMediaDto> localMediaDtoConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "local.media.importer");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(LocalMediaDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LocalMediaDto> localMediaDtoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LocalMediaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(localMediaDtoConsumerFactory());
        return factory;
    }
}
