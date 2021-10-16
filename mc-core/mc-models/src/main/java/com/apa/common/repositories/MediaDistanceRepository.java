package com.apa.common.repositories;

import com.apa.common.entities.util.MediaDistance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MediaDistanceRepository extends MongoRepository<MediaDistance, String> {
    List<MediaDistance> findByFromIdAndFromClazz(String id, String clazz);
}
