package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaDistance;

import java.util.Optional;

public interface MediaDistanceService<M extends MusicCentralMedia> {
    Optional<MediaDistance> distance(M m, PlexMedia plexMedia);
    Optional<MediaDistance> distance(M m, TidalMedia tidalMedia);
    Optional<MediaDistance> distance(M m, VolumioMedia volumioMedia);
    MediaDistance save(MediaDistance mediaDistance);

}
