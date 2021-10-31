package com.apa.common.services.media.impl.tidal;

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
public class TidalMediaDistanceService  extends AbstractMediaDistanceService<TidalMedia> {

    @Autowired
    private TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public Optional<MediaDistance> distance(TidalMedia tidalMedia, PlexMedia plexMedia) {
        if (String.valueOf(tidalMedia.getTrackNumber()).equals(plexMedia.getTrackIndex())
                    || isInvalidForDistance(plexMedia)
                    || isInvalidForDistance(plexMedia)
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(tidalMedia.getArtistName(), plexMedia.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(tidalMedia.getAlbumName(), plexMedia.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(tidalMedia.getTrackTitle(), plexMedia.getTrackTitle());
        if (isTooFar(artist, album, song)) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .artist(artist)
                .album(album)
                .song(song)
                .index(StringsDistance.builder()
                        .distance(0)
                        .from(plexMedia.getTrackIndex())
                        .to(plexMedia.getTrackIndex())
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();
        tidalMediaDistanceService.save(mediaDistance);
        return Optional.of(mediaDistance);

    }

    @Override
    public Optional<MediaDistance> distance(TidalMedia tidalMedia, TidalMedia tidalMedia2) {
        if (tidalMedia.getTrackNumber() ==tidalMedia2.getTrackNumber()
                    || isInvalidForDistance(tidalMedia)
                    || isInvalidForDistance(tidalMedia2)
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(tidalMedia.getArtistName(), tidalMedia2.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(tidalMedia.getAlbumName(), tidalMedia2.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(tidalMedia.getTrackTitle(), tidalMedia2.getTrackTitle());
        if (isTooFar(artist, album, song)) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia2.getTidalTrackId())
                        .clazz(tidalMedia2.getClass().getName())
                        .build())
                .artist(artist)
                .album(album)
                .song(song)
                .index(StringsDistance.builder()
                        .distance(0)
                        .from(String.valueOf(tidalMedia.getTrackNumber()))
                        .to(String.valueOf(tidalMedia2.getTrackNumber()))
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();

        tidalMediaDistanceService.save(mediaDistance);
        return Optional.of(mediaDistance);
    }

    @Override
    public Optional<MediaDistance> distance(TidalMedia tidalMedia, VolumioMedia volumioMedia) {
        if (!String.valueOf(tidalMedia.getTrackNumber()).equals(volumioMedia.getTrackNumber())
                    || isInvalidForDistance(tidalMedia)
                    || isInvalidForDistance(volumioMedia)
        ) {
            return Optional.empty();
        }
        StringsDistance artist = stringsDistanceService.StringsDistance(tidalMedia.getArtistName(), volumioMedia.getTrackArtist());
        StringsDistance album = stringsDistanceService.StringsDistance(tidalMedia.getAlbumName(), volumioMedia.getAlbumTitle());
        StringsDistance song = stringsDistanceService.StringsDistance(tidalMedia.getTrackTitle(), volumioMedia.getTrackTitle());
        if (isTooFar(artist, album, song)) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
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
                        .from(String.valueOf(tidalMedia.getTrackNumber()))
                        .to(volumioMedia.getTrackNumber())
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();

        tidalMediaDistanceService.save(mediaDistance);
        return Optional.of(mediaDistance);
    }

    @Override
    public boolean hasPerfectMatchRecord(TidalMedia p) {
        return hasPerfectMatchRecord(p.getTidalTrackId(), p.getClass());
    }
}
