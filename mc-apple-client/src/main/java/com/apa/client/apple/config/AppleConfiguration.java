package com.apa.client.apple.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = {"com.apa.client.apple.repository", "com.apa.common.repositories"})
public class AppleConfiguration {
}
