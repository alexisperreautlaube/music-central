package com.apa.common.repositories;

import com.apa.common.entities.media.TidalMedia;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

@JaversSpringDataAuditable
public interface TidalMediaRepository extends MongoRepository<TidalMedia, UUID> {
}
