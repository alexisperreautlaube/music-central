package com.apa.common.services.media.impl.plex;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.StringsDistance;
import com.apa.common.services.media.AbstractMediaDistanceService;
import com.apa.common.services.util.StringsDistanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlexMediaDistanceService extends AbstractMediaDistanceService<PlexMedia> {

    @Autowired
    private PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public Optional<MediaDistance> distance(PlexMedia plexMedia, PlexMedia plexMedia2) {
        if (!plexMedia.getTrackIndex().equals(plexMedia2.getTrackIndex())
                || StringUtils.isBlank(plexMedia.getArtistName())
                || StringUtils.isBlank(plexMedia.getAlbumName())
                || StringUtils.isBlank(plexMedia.getTrackTitle())
                || StringUtils.isBlank(plexMedia2.getArtistName())
                || StringUtils.isBlank(plexMedia2.getAlbumName())
                || StringUtils.isBlank(plexMedia2.getTrackTitle())
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(plexMedia.getArtistName(), plexMedia2.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(plexMedia.getAlbumName(), plexMedia2.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(plexMedia.getTrackTitle(), plexMedia2.getTrackTitle());
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        if (distanceTotal > 20 || distanceTotal == 0) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia2.getPlexId())
                        .clazz(plexMedia2.getClass().getName())
                        .build())
                .artist(artist)
                .album(album)
                .song(song)
                .index(StringsDistance.builder()
                        .distance(0)
                        .from(plexMedia.getTrackIndex())
                        .to(plexMedia2.getTrackIndex())
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();
        plexMediaDistanceService.save(mediaDistance);
        return Optional.of(mediaDistance);
    }

    @Override
    public Optional<MediaDistance> distance(PlexMedia plexMedia, TidalMedia tidalMedia) {
        if (!plexMedia.getTrackIndex().equals(tidalMedia.getTrackNumber())
                || StringUtils.isBlank(plexMedia.getArtistName())
                || StringUtils.isBlank(plexMedia.getAlbumName())
                || StringUtils.isBlank(plexMedia.getTrackTitle())
                || StringUtils.isBlank(tidalMedia.getArtistName())
                || StringUtils.isBlank(tidalMedia.getAlbumName())
                || StringUtils.isBlank(tidalMedia.getTrackTitle())
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(plexMedia.getArtistName(), tidalMedia.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(plexMedia.getAlbumName(), tidalMedia.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(plexMedia.getTrackTitle(), tidalMedia.getTrackTitle());
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        if (distanceTotal > 20 || distanceTotal == 0) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
            .from(MediaReference.builder()
                    .id(plexMedia.getPlexId())
                    .clazz(plexMedia.getClass().getName())
                    .build())
            .to(MediaReference.builder()
                    .id(tidalMedia.getTidalTrackId())
                    .clazz(tidalMedia.getClass().getName())
                    .build())
            .artist(artist)
            .album(album)
            .song(song)
                .index(StringsDistance.builder()
                        .distance(0)
                        .from(plexMedia.getTrackIndex())
                        .to(String.valueOf(tidalMedia.getTrackNumber()))
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();            plexMediaDistanceService.save(mediaDistance);
        return Optional.of(mediaDistance);

    }

    @Override
    public Optional<MediaDistance> distance(PlexMedia plexMedia, VolumioMedia volumioMedia) {
        if (!plexMedia.getTrackIndex().equals(volumioMedia.getTrackNumber())
                || StringUtils.isBlank(plexMedia.getArtistName())
                || StringUtils.isBlank(plexMedia.getAlbumName())
                || StringUtils.isBlank(plexMedia.getTrackTitle())
                || StringUtils.isBlank(volumioMedia.getTrackArtist())
                || StringUtils.isBlank(volumioMedia.getAlbumTitle())
                || StringUtils.isBlank(volumioMedia.getTrackTitle())
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(plexMedia.getArtistName(), volumioMedia.getTrackArtist());
        StringsDistance album = stringsDistanceService.StringsDistance(plexMedia.getAlbumName(), volumioMedia.getAlbumTitle());
        StringsDistance song = stringsDistanceService.StringsDistance(plexMedia.getTrackTitle(), volumioMedia.getTrackTitle());
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        if (distanceTotal > 15
                || distanceTotal == 0
                || artist.getDistance() > 5
                || album.getDistance() > 5
                || song.getDistance() > 5) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .artist(artist)
                .album(album)
                .song(song)
                .index(StringsDistance.builder()
                        .distance(0)
                        .from(plexMedia.getTrackIndex())
                        .to(volumioMedia.getTrackNumber())
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();
        plexMediaDistanceService.save(mediaDistance);
        return Optional.of(mediaDistance);

    }

    @Override
    public boolean hasPerfectMatchRecord(PlexMedia p) {
        return hasPerfectMatchRecord(p.getPlexId(), p.getClass());
    }
}
