package com.apa.client.apple.repository;

import com.apa.client.apple.entity.AppleTrack;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppleTrackRepository extends MongoRepository<AppleTrack, Long> {
    List<AppleTrack> findAppleTrackByRatingGreaterThanEqual(Short rating);
    List<AppleTrack> findAppleTrackByRatingEquals(Short rating);
}
