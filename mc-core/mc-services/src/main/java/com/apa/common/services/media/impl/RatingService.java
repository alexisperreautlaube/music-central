package com.apa.common.services.media.impl;

import com.apa.common.entities.media.Rating;
import com.apa.common.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public Optional<Rating> getLatestRating(String id, String clazz) {
        List<Rating> byMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc = ratingRepository.findByMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc(id, clazz);
        if (byMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(byMediaReferenceIdAndMediaReferenceClazzOrderByRateDateDesc.get(0));
    }
}
