package com.apa.common.repositories;

import com.apa.common.entities.media.PlexMedia;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

@JaversSpringDataAuditable
public interface PlexMediaRepository extends MongoRepository<PlexMedia, UUID> {
}
