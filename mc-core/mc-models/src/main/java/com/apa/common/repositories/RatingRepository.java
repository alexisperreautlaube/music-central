package com.apa.common.repositories;

import com.apa.common.entities.media.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating, String> {
    Optional<Rating> findByMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc(String mediaReferenceId, String mediaReferenceClezz);
}
