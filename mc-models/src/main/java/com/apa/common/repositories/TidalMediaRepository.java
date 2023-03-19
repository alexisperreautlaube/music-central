package com.apa.common.repositories;

import com.apa.common.entities.media.TidalMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TidalMediaRepository extends MongoRepository<TidalMedia, String> {
    List<TidalMedia> findByArtistNameAndAlbumNameAndTrackTitleAndTrackNumber(String artistName, String albumName, String trackTitle, Integer index);

}
