package com.apa.common.config;

import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Import({
        MongoConfiguration.class,
})
@EnableMongoRepositories(basePackages = "com.apa.common.repositories")
public class MusicCentralModelsConfiguration {
}
