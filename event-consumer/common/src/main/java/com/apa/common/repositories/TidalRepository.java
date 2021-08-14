package com.apa.common.repositories;

import com.apa.common.entities.TidalMedia;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

@JaversSpringDataAuditable
public interface TidalRepository extends MongoRepository<TidalMedia, UUID> {
}
