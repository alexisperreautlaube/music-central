package com.apa.common.services.media.impl.tidal;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.StringsDistance;
import com.apa.common.services.media.PerfectMatchFinder;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TidalPerfectMatchFinder implements PerfectMatchFinder<TidalMedia> {

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private TidalMediaDistanceService mediaDistanceService;

    @Override
    public List<PlexMedia> findPlexMatch(TidalMedia tidalMedia) {
        if (!tidalMediaService.exist(tidalMedia)) {
            return Collections.emptyList();
        }
        List<PlexMedia> perfectMatch = plexMediaService
                .findPerfectMatch(
                        tidalMedia.getArtistName(),
                        tidalMedia.getAlbumName(),
                        tidalMedia.getTrackTitle(),
                        String.valueOf(tidalMedia.getTrackNumber()));

        perfectMatch.forEach(plexMedia -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId().toString())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(tidalMedia.getArtistName())
                        .to(plexMedia.getArtistName())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(tidalMedia.getAlbumName())
                        .to(plexMedia.getAlbumName())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(String.valueOf(tidalMedia.getTrackNumber()))
                        .to(plexMedia.getTrackIndex())
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(tidalMedia.getTrackTitle())
                        .to(plexMedia.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }

    @Override
    public List<TidalMedia> findTidalMatch(TidalMedia tidalMedia) {
        if (!tidalMediaService.exist(tidalMedia)) {
            return Collections.emptyList();
        }
        List<TidalMedia> perfectMatch = tidalMediaService.findPerfectMatch(
                tidalMedia.getArtistName(),
                tidalMedia.getAlbumName(),
                tidalMedia.getTrackTitle(),
                String.valueOf(tidalMedia.getTrackNumber())).stream()
                .filter(p -> !tidalMedia.equals(p))
                .collect(Collectors.toList());

        perfectMatch.forEach(volumioMedia -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTidalTrackId())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(volumioMedia.getArtistName())
                        .to(tidalMedia.getArtistName())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(volumioMedia.getAlbumName())
                        .to(tidalMedia.getAlbumName())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(String.valueOf(tidalMedia.getTrackNumber()))
                        .to(String.valueOf(tidalMedia.getTrackNumber()))
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(volumioMedia.getTrackTitle())
                        .to(tidalMedia.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }

    @Override
    public List<VolumioMedia> findVolumioMatch(TidalMedia tidalMediaFrom) {
        if (!tidalMediaService.exist(tidalMediaFrom)) {
            return Collections.emptyList();
        }
        List<VolumioMedia> perfectMatch = volumioMediaService.findPerfectMatch(
                tidalMediaFrom.getArtistName(),
                tidalMediaFrom.getAlbumName(),
                tidalMediaFrom.getTrackTitle(),
                String.valueOf(tidalMediaFrom.getTrackNumber()));

        perfectMatch.forEach(volumioMediaTo -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMediaFrom.getTidalTrackId())
                        .clazz(tidalMediaFrom.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMediaTo.getTrackUri())
                        .clazz(volumioMediaTo.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(tidalMediaFrom.getArtistName())
                        .to(volumioMediaTo.getTrackArtist())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(tidalMediaFrom.getAlbumName())
                        .to(volumioMediaTo.getAlbumTitle())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(String.valueOf(tidalMediaFrom.getTrackNumber()))
                        .to(volumioMediaTo.getTrackNumber())
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(tidalMediaFrom.getTrackTitle())
                        .to(volumioMediaTo.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }
}
