package com.apa.common.config;

import org.javers.spring.boot.mongo.JaversMongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Import({
        MongoConfiguration.class,
        JaversMongoAutoConfiguration.class
})
@ComponentScan(basePackages = "com.apa.common.services")
@EnableMongoRepositories(basePackages = "com.apa.common.repositories")
public class MusicCentralServicesConfiguration {
}
