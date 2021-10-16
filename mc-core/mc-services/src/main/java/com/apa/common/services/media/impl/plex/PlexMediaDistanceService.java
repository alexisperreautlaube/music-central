package com.apa.common.services.media.impl.plex;

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
public class PlexMediaDistanceService extends AbstractMediaDistanceService<PlexMedia> {

    @Autowired
    private PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public MediaDistance distance(PlexMedia plexMedia, PlexMedia plexMedia2) {
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia2.getPlexId())
                        .clazz(plexMedia2.getClass().getName())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(plexMedia.getArtistName(), plexMedia2.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(plexMedia.getAlbumName(), plexMedia2.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(plexMedia.getTrackTitle(), plexMedia2.getTrackTitle()))
                .build();
        plexMediaDistanceService.save(mediaDistance);
        return mediaDistance;
    }

    @Override
    public MediaDistance distance(PlexMedia plexMedia, TidalMedia tidalMedia) {
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass().getName())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(plexMedia.getArtistName(), tidalMedia.getArtistName()))
                .album(stringsDistanceService
                        .StringsDistance(plexMedia.getAlbumName(), tidalMedia.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(plexMedia.getTrackTitle(), tidalMedia.getTrackTitle()))
                .build();
        plexMediaDistanceService.save(mediaDistance);
        return mediaDistance;
    }

    @Override
    public MediaDistance distance(PlexMedia plexMedia, VolumioMedia volumioMedia) {
        MediaDistance mediaDistance = MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass().getName())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(plexMedia.getArtistName(), volumioMedia.getTrackArtist()))
                .album(stringsDistanceService
                        .StringsDistance(plexMedia.getAlbumName(), volumioMedia.getAlbumTitle()))
                .song(stringsDistanceService
                        .StringsDistance(plexMedia.getTrackTitle(), volumioMedia.getTrackTitle()))
                .build();
        plexMediaDistanceService.save(mediaDistance);
        return mediaDistance;
    }

    @Override
    public boolean hasPerfectMatchRecord(PlexMedia p) {
        return hasPerfectMatchRecord(p.getPlexId(), p.getClass());
    }
}
