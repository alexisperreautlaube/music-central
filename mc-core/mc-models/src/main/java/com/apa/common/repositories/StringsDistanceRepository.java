package com.apa.common.repositories;

import com.apa.common.entities.util.StringsDistance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StringsDistanceRepository  extends MongoRepository<StringsDistance, String> {
    Optional<StringsDistance> findByFromAndTo(String from, String to);
}
