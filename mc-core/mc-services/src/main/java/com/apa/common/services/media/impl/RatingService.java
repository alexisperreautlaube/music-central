package com.apa.common.services.media.impl;

import com.apa.common.entities.media.Rating;
import com.apa.common.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MongoTemplate template;

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public Optional<Rating> getLatestRating(String id, String clazz) {
        return ratingRepository.findByMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc(id, clazz);
    }
}
