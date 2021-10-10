package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaDistance;

public interface MediaDistanceService<M extends MusicCentralMedia> {
    MediaDistance distance(M m, PlexMedia plexMedia);
    MediaDistance distance(M m, TidalMedia tidalMedia);
    MediaDistance distance(M m, VolumioMedia volumioMedia);
}
