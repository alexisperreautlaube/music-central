package com.apa.common.repositories;

import com.apa.common.entities.util.ProducedMatch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProducedMatchRepository extends MongoRepository<ProducedMatch, String> {
}
