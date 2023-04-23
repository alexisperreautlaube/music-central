package com.apa.common.entities.equalizer.repository;

import com.apa.common.entities.equalizer.entity.Equalizer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EqualizerRepository extends MongoRepository<Equalizer, String> {

}
