package com.apa.client.apple.repository;

import com.apa.client.apple.entity.AppleAvailableTrack;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppleAvailableTrackRepository extends MongoRepository<AppleAvailableTrack, Long> {
    List<AppleAvailableTrack> findAppleTrackByRatingGreaterThanEqualOrderByReleaseDateDesc(Short rating);
    List<AppleAvailableTrack> findAppleTrackByRatingEqualsOrderByReleaseDateDesc(Short rating);

    int countAppleAvailableTrackByArtistEqualsIgnoreCaseAndRatingGreaterThanEqual(String artist, Short rating);

    int countAppleAvailableTrackByArtistEqualsIgnoreCaseAndAndAlbumEqualsIgnoreCaseAndRatingGreaterThanEqual(String artist, String album, Short rating);
}
