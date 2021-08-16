package com.apa.events.repositories;

import com.apa.events.entities.MusicCentralEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MusicCentralEventRepository extends MongoRepository<MusicCentralEvent, UUID> {
}
