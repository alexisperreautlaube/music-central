package com.apa.common.services.media.impl.plex;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.services.media.AbstractMediaDistanceService;
import com.apa.common.services.util.StringsDistanceService;
import org.springframework.beans.factory.annotation.Autowired;

public class PlexMediaDistanceService extends AbstractMediaDistanceService<PlexMedia> {

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public MediaDistance distance(PlexMedia plexMedia, PlexMedia plexMedia2) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia2.getPlexId())
                        .clazz(plexMedia2.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(plexMedia.getArtistName(), plexMedia2.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(plexMedia.getAlbumName(), plexMedia2.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(plexMedia.getTrackTitle(), plexMedia2.getTrackTitle()))
                .build();
    }

    @Override
    public MediaDistance distance(PlexMedia plexMedia, TidalMedia tidalMedia) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(plexMedia.getArtistName(), tidalMedia.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(plexMedia.getAlbumName(), tidalMedia.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(plexMedia.getTrackTitle(), tidalMedia.getTrackTitle()))
                .build();
    }

    @Override
    public MediaDistance distance(PlexMedia plexMedia, VolumioMedia volumioMedia) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(plexMedia.getArtistName(), volumioMedia.getTrackArtist()))
                .album(stringsDistanceService
                        .StringsDistance(plexMedia.getAlbumName(), volumioMedia.getAlbumTitle()))
                .song(stringsDistanceService
                        .StringsDistance(plexMedia.getTrackTitle(), volumioMedia.getTrackTitle()))
                .build();
    }
}
