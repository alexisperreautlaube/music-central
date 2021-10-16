package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.StringsDistance;
import com.apa.common.services.media.PerfectMatchFinder;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VolumioPerfectMatchFinder implements PerfectMatchFinder<VolumioMedia>{

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private VolumioMediaDistanceService mediaDistanceService;

    @Override
    public List<PlexMedia> findPlexMatch(VolumioMedia volumioMedia) {
        List<PlexMedia> perfectMatch = plexMediaService
                .findPerfectMatch(
                        volumioMedia.getTrackArtist(),
                        volumioMedia.getAlbumTitle(),
                        volumioMedia.getTrackTitle(),
                        volumioMedia.getTrackNumber());

        perfectMatch.forEach(plexMedia -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId().toString())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(volumioMedia.getTrackArtist())
                        .to(plexMedia.getArtistName())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(volumioMedia.getAlbumTitle())
                        .to(plexMedia.getAlbumName())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(volumioMedia.getTrackNumber())
                        .to(String.valueOf(plexMedia.getTrackIndex()))
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(volumioMedia.getTrackTitle())
                        .to(plexMedia.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }

    @Override
    public List<TidalMedia> findTidalMatch(VolumioMedia volumioMedia) {
        List<TidalMedia> perfectMatch = tidalMediaService.findPerfectMatch(
                volumioMedia.getTrackArtist(),
                volumioMedia.getAlbumTitle(),
                volumioMedia.getTrackTitle(),
                volumioMedia.getTrackNumber());

        perfectMatch.forEach(tidalMedia -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(volumioMedia.getTrackArtist())
                        .to(tidalMedia.getArtistName())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(volumioMedia.getAlbumTitle())
                        .to(tidalMedia.getAlbumName())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(volumioMedia.getTrackNumber())
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
    public List<VolumioMedia> findVolumioMatch(VolumioMedia volumioMedia) {
        List<VolumioMedia> perfectMatch = volumioMediaService.findPerfectMatch(
                volumioMedia.getTrackArtist(),
                volumioMedia.getAlbumTitle(),
                volumioMedia.getTrackTitle(),
                volumioMedia.getTrackNumber()).stream()
                .filter(p -> !volumioMedia.equals(p))
                .collect(Collectors.toList());

        perfectMatch.forEach(volumioMediaTo -> mediaDistanceService.save(MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMediaTo.getTrackUri())
                        .clazz(volumioMediaTo.getClass().getName())
                        .build())
                .artist(StringsDistance.builder()
                        .from(volumioMedia.getTrackArtist())
                        .to(volumioMediaTo.getTrackArtist())
                        .distance(0)
                        .build())
                .album(StringsDistance.builder()
                        .from(volumioMedia.getAlbumTitle())
                        .to(volumioMediaTo.getAlbumTitle())
                        .distance(0)
                        .build())
                .index(StringsDistance.builder()
                        .from(volumioMedia.getTrackNumber())
                        .to(volumioMediaTo.getTrackNumber())
                        .distance(0)
                        .build())
                .song(StringsDistance.builder()
                        .from(volumioMedia.getTrackTitle())
                        .to(volumioMediaTo.getTrackTitle())
                        .distance(0)
                        .build())
                .matchStatus(MatchStatus.PERFECT_MATCH)
                .build()));
        return perfectMatch;
    }
}
