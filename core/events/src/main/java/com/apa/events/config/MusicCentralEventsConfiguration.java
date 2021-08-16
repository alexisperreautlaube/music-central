package com.apa.events.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = "com.apa.events.executor")
@EnableMongoRepositories(basePackages = "com.apa.events.repositories")
public class MusicCentralEventsConfiguration {
}
