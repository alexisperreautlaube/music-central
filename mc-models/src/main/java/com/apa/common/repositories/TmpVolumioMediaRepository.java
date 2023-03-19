package com.apa.common.repositories;

import com.apa.common.entities.media.TmpVolumioMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TmpVolumioMediaRepository extends MongoRepository<TmpVolumioMedia, String> {

}
