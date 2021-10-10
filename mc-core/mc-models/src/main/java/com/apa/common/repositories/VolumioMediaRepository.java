package com.apa.common.repositories;

import com.apa.common.entities.media.VolumioMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VolumioMediaRepository extends MongoRepository<VolumioMedia, String> {
}
