package com.apa.client.apple.repository;

import com.apa.client.apple.entity.AppleEqualizer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppleEqualizerRepository extends MongoRepository<AppleEqualizer, String> {
    Optional<AppleEqualizer> findAppleEqualizerByName(String name);

}
