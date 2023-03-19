package com.apa.common.repositories;

import com.apa.common.entities.media.MediaError;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediaErrorRepository extends MongoRepository<MediaError, String> {

    Optional<MediaError> findByUri(String uri);
    void deleteByUri(String uri);
}
