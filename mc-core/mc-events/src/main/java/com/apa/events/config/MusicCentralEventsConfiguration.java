package com.apa.events.config;

import com.apa.common.config.MusicCentralServicesConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Import(MusicCentralServicesConfiguration.class)
@ComponentScan(basePackages = "com.apa.events.executor")
@EnableMongoRepositories(basePackages = "com.apa.events.repositories")
public class MusicCentralEventsConfiguration {
}
