package com.apa.common.repositories;

import com.apa.common.entities.media.VolumioMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VolumioMediaRepository extends MongoRepository<VolumioMedia, String> {
    List<VolumioMedia> findByAlbumArtistAndAlbumTitle(String artistName, String albumName);
    List<VolumioMedia> findByAlbumArtistAndAlbumTitleAndTrackTitleAndTrackNumber(String artistName, String albumName, String trackTitle, String index);
    Optional<VolumioMedia> findFirstByAlbumUri(String uti);
}
