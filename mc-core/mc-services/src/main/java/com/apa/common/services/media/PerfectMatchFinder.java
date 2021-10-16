package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;

import java.util.List;

public interface PerfectMatchFinder<M extends MusicCentralMedia> {
    List<PlexMedia> findPlexMatch(M m);
    List<TidalMedia> findTidalMatch(M m);
    List<VolumioMedia> findVolumioMatch(M m);
}
