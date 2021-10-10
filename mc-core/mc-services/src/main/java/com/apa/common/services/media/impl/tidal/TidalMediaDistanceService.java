package com.apa.common.services.media.impl.tidal;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.services.media.AbstractMediaDistanceService;
import com.apa.common.services.util.StringsDistanceService;
import org.springframework.beans.factory.annotation.Autowired;

public class TidalMediaDistanceService  extends AbstractMediaDistanceService<TidalMedia> {

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public MediaDistance distance(TidalMedia tidalMedia, PlexMedia plexMedia) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(tidalMedia.getArtistName(), plexMedia.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(tidalMedia.getAlbumName(), plexMedia.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(tidalMedia.getTrackTitle(), plexMedia.getTrackTitle()))
                .build();
    }

    @Override
    public MediaDistance distance(TidalMedia tidalMedia, TidalMedia tidalMedia2) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia2.getTidalTrackId())
                        .clazz(tidalMedia2.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(tidalMedia.getArtistName(), tidalMedia2.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(tidalMedia.getAlbumName(), tidalMedia2.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(tidalMedia.getTrackTitle(), tidalMedia2.getTrackTitle()))
                .build();
    }

    @Override
    public MediaDistance distance(TidalMedia tidalMedia, VolumioMedia volumioMedia) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(tidalMedia.getArtistName(), volumioMedia.getTrackArtist()))
                .album(stringsDistanceService
                        .StringsDistance(tidalMedia.getAlbumName(), volumioMedia.getAlbumTitle()))
                .song(stringsDistanceService
                        .StringsDistance(tidalMedia.getTrackTitle(), volumioMedia.getTrackTitle()))
                .build();
    }
}
