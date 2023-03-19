package com.apa.common.repositories;

import com.apa.common.entities.media.TmpMediaError;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TmpMediaErrorRepository extends MongoRepository<TmpMediaError, String> {

}
