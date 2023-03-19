package com.apa.common.repositories;

import com.apa.common.entities.media.PlexMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlexMediaRepository extends MongoRepository<PlexMedia, String> {
    List<PlexMedia> findByArtistNameAndAlbumNameAndTrackTitleAndTrackIndex(String artistName, String albumName, String trackTitle, String index);
}
