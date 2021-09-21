package com.apa.common.error;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ErrorMessageRepository extends MongoRepository<ErrorMessage, UUID> {
}
