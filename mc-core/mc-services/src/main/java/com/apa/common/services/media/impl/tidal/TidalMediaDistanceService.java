package com.apa.common.services.media.impl.tidal;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.services.media.AbstractMediaDistanceService;
import com.apa.common.services.util.StringsDistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TidalMediaDistanceService  extends AbstractMediaDistanceService<TidalMedia> {

    @Autowired
    private TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public MediaDistance distance(TidalMedia tidalMedia, PlexMedia plexMedia) {
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(tidalMedia.getArtistName(), plexMedia.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(tidalMedia.getAlbumName(), plexMedia.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(tidalMedia.getTrackTitle(), plexMedia.getTrackTitle()))
                .build();
        tidalMediaDistanceService.save(mediaDistance);
        return mediaDistance;
    }

    @Override
    public MediaDistance distance(TidalMedia tidalMedia, TidalMedia tidalMedia2) {
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia2.getTidalTrackId())
                        .clazz(tidalMedia2.getClass().getName())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(tidalMedia.getArtistName(), tidalMedia2.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(tidalMedia.getAlbumName(), tidalMedia2.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(tidalMedia.getTrackTitle(), tidalMedia2.getTrackTitle()))
                .build();
        tidalMediaDistanceService.save(mediaDistance);
        return mediaDistance;
    }

    @Override
    public MediaDistance distance(TidalMedia tidalMedia, VolumioMedia volumioMedia) {
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(tidalMedia.getArtistName(), volumioMedia.getTrackArtist()))
                .album(stringsDistanceService
                        .StringsDistance(tidalMedia.getAlbumName(), volumioMedia.getAlbumTitle()))
                .song(stringsDistanceService
                        .StringsDistance(tidalMedia.getTrackTitle(), volumioMedia.getTrackTitle()))
                .build();
        tidalMediaDistanceService.save(mediaDistance);
        return mediaDistance;
    }

    @Override
    public boolean hasPerfectMatchRecord(TidalMedia p) {
        return hasPerfectMatchRecord(p.getTidalTrackId(), p.getClass());
    }
}
