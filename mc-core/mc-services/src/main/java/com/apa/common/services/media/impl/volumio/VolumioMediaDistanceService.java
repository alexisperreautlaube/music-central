package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.entities.util.StringsDistance;
import com.apa.common.repositories.MediaDistanceRepository;
import com.apa.common.services.media.AbstractMediaDistanceService;
import com.apa.common.services.util.StringsDistanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VolumioMediaDistanceService extends AbstractMediaDistanceService<VolumioMedia> {

    @Autowired
    private MediaDistanceRepository mediaDistanceRepository;

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public Optional<MediaDistance> distance(VolumioMedia volumioMedia, PlexMedia plexMedia) {
        if (volumioMedia.getTrackNumber().equals(plexMedia.getTrackIndex())
                || StringUtils.isBlank(plexMedia.getArtistName())
                || StringUtils.isBlank(plexMedia.getAlbumName())
                || StringUtils.isBlank(plexMedia.getTrackTitle())
                || StringUtils.isBlank(plexMedia.getArtistName())
                || StringUtils.isBlank(plexMedia.getAlbumName())
                || StringUtils.isBlank(plexMedia.getTrackTitle())
        ) {
            return Optional.empty();
        }

        StringsDistance artist = stringsDistanceService.StringsDistance(volumioMedia.getTrackArtist(), plexMedia.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(volumioMedia.getAlbumTitle(), plexMedia.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(volumioMedia.getTrackTitle(), plexMedia.getTrackTitle());
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        if (distanceTotal > 20 || distanceTotal == 0) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
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
                        .from(volumioMedia.getTrackNumber())
                        .to(plexMedia.getTrackIndex())
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();

        save(mediaDistance);
        return Optional.of(mediaDistance);
    }

    @Override
    public Optional<MediaDistance> distance(VolumioMedia volumioMedia, TidalMedia tidalMedia) {
        if (volumioMedia.getTrackNumber().equals(String.valueOf(tidalMedia.getTrackNumber()))
                || StringUtils.isBlank(volumioMedia.getTrackArtist())
                || StringUtils.isBlank(volumioMedia.getAlbumTitle())
                || StringUtils.isBlank(volumioMedia.getTrackTitle())
                || StringUtils.isBlank(tidalMedia.getArtistName())
                || StringUtils.isBlank(tidalMedia.getAlbumName())
                || StringUtils.isBlank(tidalMedia.getTrackTitle())
        ) {
            return Optional.empty();
        }

        StringsDistance artist = stringsDistanceService.StringsDistance(volumioMedia.getTrackArtist(), tidalMedia.getArtistName());
        StringsDistance album = stringsDistanceService.StringsDistance(volumioMedia.getAlbumTitle(), tidalMedia.getAlbumName());
        StringsDistance song = stringsDistanceService.StringsDistance(volumioMedia.getTrackTitle(), tidalMedia.getTrackTitle());
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        if (distanceTotal > 20 || distanceTotal == 0) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
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
                        .from(volumioMedia.getTrackNumber())
                        .to(String.valueOf(tidalMedia.getTrackNumber()))
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();

        save(mediaDistance);
        return Optional.of(mediaDistance);
    }

    @Override
    public Optional<MediaDistance> distance(VolumioMedia volumioMedia, VolumioMedia volumioMedia2) {
        if (volumioMedia.getTrackNumber().equals(volumioMedia2.getTrackNumber())
                || StringUtils.isBlank(volumioMedia.getTrackArtist())
                || StringUtils.isBlank(volumioMedia.getAlbumTitle())
                || StringUtils.isBlank(volumioMedia.getTrackTitle())
                || StringUtils.isBlank(volumioMedia2.getTrackArtist())
                || StringUtils.isBlank(volumioMedia2.getAlbumTitle())
                || StringUtils.isBlank(volumioMedia2.getTrackTitle())
        ) {
            return Optional.empty();
        }

        StringsDistance artist = stringsDistanceService.StringsDistance(volumioMedia.getTrackArtist(), volumioMedia2.getTrackArtist());
        StringsDistance album = stringsDistanceService.StringsDistance(volumioMedia.getAlbumTitle(), volumioMedia2.getAlbumTitle());
        StringsDistance song = stringsDistanceService.StringsDistance(volumioMedia.getTrackTitle(), volumioMedia2.getTrackTitle());
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        if (distanceTotal > 20 || distanceTotal == 0) {
            return Optional.empty();
        }
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia2.getTrackUri())
                        .clazz(volumioMedia2.getClass().getName())
                        .build())
                .artist(artist)
                .album(album)
                .song(song)
                .index(StringsDistance.builder()
                        .distance(0)
                        .from(volumioMedia.getTrackNumber())
                        .to(volumioMedia2.getTrackNumber())
                        .build())
                .matchStatus(MatchStatus.AUTOMATIC_MATCH)
                .build();

        save(mediaDistance);
        return Optional.of(mediaDistance);
    }

    @Override
    public boolean hasPerfectMatchRecord(VolumioMedia p) {
        return hasPerfectMatchRecord(p.getTrackUri(), p.getClass());
    }

    public List<MediaDistance> getMatches(VolumioMedia volumioMedia) {
        return mediaDistanceRepository.findByFromAndTo(volumioMedia.getTrackUri(), volumioMedia.getClass().getName());
    }
}
