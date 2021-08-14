package com.apa.common.repositories;

import com.apa.common.entities.LocalMedia;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

@JaversSpringDataAuditable
public interface LocalRepository extends MongoRepository<LocalMedia, UUID> {
}
