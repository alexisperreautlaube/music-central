package com.apa.common.repositories;

import com.apa.common.entities.util.MediaDistance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MediaDistanceRepository extends MongoRepository<MediaDistance, String> {
    List<MediaDistance> findByFromIdAndFromClazz(String id, String clazz);

    @Query("{ $or : " +
                "[" +
                    "{$and : [ " +
                        "{ 'from._id' : ?0 }," +
                        "{ 'from.clazz' : ?1 }, " +
                        "{ 'artist.distance' : {$lt : 4 }}, " +
                        "{ 'album.distance'  : {$lt : 4 }}, " +
                        "{ 'song.distance' : {$lt : 5 }} " +
                    "]}," +
                    "{$and : [ " +
                        "{ 'to._id' : ?0 }," +
                        "{ 'to.clazz' : ?1 } " +
                        "{ 'artist.distance' : {$lt : 4 }}, " +
                        "{ 'album.distance'  : {$lt : 4 }}, " +
                        "{ 'song.distance' : {$lt : 5 }} " +
                    "]}" +
                "]" +
            "}")
    List<MediaDistance> findByFromAndTo(String id, String clazz);
}
