package com.apa.common.repositories;

import com.apa.common.entities.media.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc(String mediaReferenceId, String mediaReferenceClezz);
}
