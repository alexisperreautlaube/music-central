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
                || isValidForDistance(plexMedia)
                || isValidForDistance(plexMedia2)
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(plexMedia.getArtistName(), plexMedia2.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(plexMedia.getAlbumName(), plexMedia2.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(plexMedia.getTrackTitle(), plexMedia2.getTrackTitle());
        if (isTooFar(artist, album, song)) {
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
                || isValidForDistance(plexMedia)
                || isValidForDistance(tidalMedia)
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(plexMedia.getArtistName(), tidalMedia.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(plexMedia.getAlbumName(), tidalMedia.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(plexMedia.getTrackTitle(), tidalMedia.getTrackTitle());
        if (isTooFar(artist, album, song)) {
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
                || isValidForDistance(plexMedia)
                || isValidForDistance(volumioMedia)
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(plexMedia.getArtistName(), volumioMedia.getTrackArtist());
        StringsDistance album = stringsDistanceService.StringsDistance(plexMedia.getAlbumName(), volumioMedia.getAlbumTitle());
        StringsDistance song = stringsDistanceService.StringsDistance(plexMedia.getTrackTitle(), volumioMedia.getTrackTitle());
        if (isTooFar(artist, album, song)) {
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
