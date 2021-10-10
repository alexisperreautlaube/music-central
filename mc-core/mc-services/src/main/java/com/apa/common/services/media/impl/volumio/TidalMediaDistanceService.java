package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.services.media.AbstractMediaDistanceService;
import com.apa.common.services.util.StringsDistanceService;
import org.springframework.beans.factory.annotation.Autowired;

public class TidalMediaDistanceService extends AbstractMediaDistanceService<VolumioMedia> {

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Override
    public MediaDistance distance(VolumioMedia volumioMedia, PlexMedia plexMedia) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(plexMedia.getPlexId())
                        .clazz(plexMedia.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(volumioMedia.getTrackArtist(), plexMedia.getTrackTitle()))
                .album(stringsDistanceService
                        .StringsDistance(volumioMedia.getAlbumTitle(), plexMedia.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(volumioMedia.getTrackTitle(), plexMedia.getTrackTitle()))
                .build();
    }

    @Override
    public MediaDistance distance(VolumioMedia volumioMedia, TidalMedia tidalMedia) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(tidalMedia.getTidalTrackId())
                        .clazz(tidalMedia.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(volumioMedia.getTrackArtist(), tidalMedia.getTrackTitle()))
                .album(stringsDistanceService
                        .StringsDistance(volumioMedia.getAlbumTitle(), tidalMedia.getAlbumName()))
                .song(stringsDistanceService
                        .StringsDistance(volumioMedia.getTrackTitle(), tidalMedia.getTrackTitle()))
                .build();
    }

    @Override
    public MediaDistance distance(VolumioMedia volumioMedia, VolumioMedia volumioMedia2) {
        return MediaDistance.builder()
                .from(MediaReference.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass())
                        .build())
                .to(MediaReference.builder()
                        .id(volumioMedia2.getTrackUri())
                        .clazz(volumioMedia2.getClass())
                        .build())
                .artist(stringsDistanceService
                        .StringsDistance(volumioMedia.getTrackArtist(), volumioMedia2.getTrackTitle()))
                .album(stringsDistanceService
                        .StringsDistance(volumioMedia.getAlbumTitle(), volumioMedia2.getAlbumTitle()))
                .song(stringsDistanceService
                        .StringsDistance(volumioMedia.getTrackTitle(), volumioMedia2.getTrackTitle()))
                .build();
    }
}
