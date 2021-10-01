package com.apa.common.repositories;

import com.apa.common.entities.media.LocalMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LocalMediaRepository extends MongoRepository<LocalMedia, String> {
}
