package com.apa.consumer;

import com.apa.importer.ImporterSpringBootTestApplication;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.kafka.consumer.group-id=media.importer",
        "spring.kafka.topic.local.media.importer=local.media.importer",
        "spring.kafka.topic.plex.media.importer=local.plex.importer",
        "spring.kafka.topic.tidal.media.importer=local.tidal.importer"
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ImporterSpringBootTestApplication.class)
public class AbstractConsumerIT {

    @Getter
    @Container
    public KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
}
