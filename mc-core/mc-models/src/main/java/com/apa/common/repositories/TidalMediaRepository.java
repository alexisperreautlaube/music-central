package com.apa.common.repositories;

import com.apa.common.entities.media.TidalMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TidalMediaRepository extends MongoRepository<TidalMedia, String> {
    Optional<TidalMedia> findByTidalTrackId(String tidalTrackId);
}
