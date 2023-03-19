package com.apa.common.repositories;

import com.apa.common.entities.media.AvailableMedias;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvailableMediasRepository extends MongoRepository<AvailableMedias, String> {
    List<AvailableMedias> findByRatingGreaterThan(Integer rating);
    List<AvailableMedias> findByRatingOrderByReleaseDateDesc(Integer rating);
}
