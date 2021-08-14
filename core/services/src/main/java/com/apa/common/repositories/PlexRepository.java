package com.apa.common.repositories;

import com.apa.common.entities.PlexMedia;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

@JaversSpringDataAuditable
public interface PlexRepository extends MongoRepository<PlexMedia, UUID> {
}
