package com.apa.common.services.media.impl.plex;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.StringsDistance;
import com.apa.common.services.media.PerfectMatchFinder;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlexPerfectMatchFinder implements PerfectMatchFinder<PlexMedia>{

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private PlexMediaDistanceService mediaDistanceService;

    @Override
    public List<PlexMedia> findPlexMatch(PlexMedia plexMediaFrom) {
        if (!plexMediaService.exist(plexMediaFrom)) {
            return Collections.emptyList();
        }
        List<PlexMedia> perfectMatch = plexMediaService
                .findPerfectMatch(
                        plexMediaFrom.getArtistName(),
                        plexMediaFrom.getAlbumName(),
                        plexMediaFrom.getTrackTitle(),
                        plexMediaFrom.getTrackIndex()).stream()
                .filter(p -> !plexMediaFrom.equals(p))
                .collect(Collectors.toList());

        perfectMatch.forEach(plexMedia -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMediaFrom.getPlexId())
                        .clazz(plexMediaFrom.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(plexMediaFrom.getArtistName())
                        .to(plexMedia.getArtistName())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(plexMediaFrom.getAlbumName())
                        .to(plexMedia.getAlbumName())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(plexMediaFrom.getTrackIndex())
                        .to(plexMedia.getTrackIndex())
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(plexMediaFrom.getTrackTitle())
                        .to(plexMedia.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }

    @Override
    public List<TidalMedia> findTidalMatch(PlexMedia plexMedia) {
        if (!plexMediaService.exist(plexMedia)) {
            return Collections.emptyList();
        }
        List<TidalMedia> perfectMatch = tidalMediaService
                .findPerfectMatch(
                        plexMedia.getArtistName(),
                        plexMedia.getAlbumName(),
                        plexMedia.getTrackTitle(),
                        plexMedia.getTrackIndex());


        perfectMatch.forEach(tidalMedia -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(plexMedia.getArtistName())
                        .to(tidalMedia.getArtistName())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(plexMedia.getAlbumName())
                        .to(tidalMedia.getAlbumName())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(plexMedia.getTrackIndex())
                        .to(String.valueOf(tidalMedia.getTrackNumber()))
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(plexMedia.getTrackTitle())
                        .to(tidalMedia.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }

    @Override
    public List<VolumioMedia> findVolumioMatch(PlexMedia plexMedia) {
        if (!plexMediaService.exist(plexMedia)) {
            return Collections.emptyList();
        }
        List<VolumioMedia> perfectMatch = volumioMediaService.findPerfectMatch(
                plexMedia.getArtistName(),
                plexMedia.getAlbumName(),
                plexMedia.getTrackTitle(),
                plexMedia.getTrackIndex());

        perfectMatch.forEach(volumioMediaTo -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMediaTo.getTrackUri())
                        .clazz(volumioMediaTo.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(plexMedia.getArtistName())
                        .to(volumioMediaTo.getTrackArtist())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(plexMedia.getAlbumName())
                        .to(volumioMediaTo.getAlbumTitle())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(plexMedia.getTrackIndex())
                        .to(volumioMediaTo.getTrackNumber())
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(plexMedia.getTrackTitle())
                        .to(volumioMediaTo.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }
}
