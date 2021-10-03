package com.apa.consumer.config;

import com.apa.common.msg.InputMessage;
import com.apa.consumer.error.McKafkaErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;


    public ConsumerFactory<String, InputMessage> tidalMediaDtoConsumerFactory() {
        Map<String, Object> props = initCommonProperties();
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(InputMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InputMessage> tidalMediaDtoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InputMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tidalMediaDtoConsumerFactory());
        setFactoryCommonProperties(factory);
        return factory;
    }

    @NotNull
    private Map<String, Object> initCommonProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        return props;
    }

    private void setFactoryCommonProperties(ConcurrentKafkaListenerContainerFactory<String, ?> factory) {
        factory.setErrorHandler(new McKafkaErrorHandler());
    }
}
