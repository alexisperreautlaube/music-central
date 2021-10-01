package com.apa.common.repositories;

import com.apa.common.entities.media.TidalMedia;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TidalMediaRepository extends MongoRepository<TidalMedia, String> {
}
