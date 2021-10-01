package com.apa.common.repositories;

import com.apa.common.entities.media.PlexMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlexMediaRepository extends MongoRepository<PlexMedia, String> {
}
