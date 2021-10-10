package com.apa.events.mapper;

import com.apa.common.entities.media.PlexMedia;
import com.apa.core.dto.media.PlexMediaDto;

public class PlexMediaMapper {
    public static PlexMedia toPlexMedia(PlexMediaDto plexMediaDto) {
        return PlexMedia.builder()
                .plexId(plexMediaDto.getPlexId())
                .artistName(plexMediaDto.getArtistName())
                .albumName(plexMediaDto.getAlbumName())
                .trackTitle(plexMediaDto.getTrackTitle())
                .addedAt(plexMediaDto.getAddedAt())
                .albumIndex(plexMediaDto.getAlbumIndex())
                .albumLastViewedAt(plexMediaDto.getAlbumLastViewedAt())
                .albumRatingKey(plexMediaDto.getAlbumRatingKey())
                .albumSortTitle(plexMediaDto.getAlbumSortTitle())
                .albumViewCount(plexMediaDto.getAlbumViewCount())
                .albumOriginallyAvailableAt(plexMediaDto.getAlbumOriginallyAvailableAt())
                .artistKey(plexMediaDto.getArtistKey())
                .albumStudio(plexMediaDto.getAlbumStudio())
                .albumYear(plexMediaDto.getAlbumYear())

                .trackAddedAt(plexMediaDto.getTrackAddedAt())
                .trackIndex(plexMediaDto.getTrackIndex())
                .trackLastViewedAt(plexMediaDto.getTrackLastViewedAt())
                .trackSortTitle(plexMediaDto.getTrackSortTitle())
                .trackUpdatedAt(plexMediaDto.getTrackUpdatedAt())
                .trackViewCount(plexMediaDto.getTrackViewCount())
                .trackViewedAt(plexMediaDto.getTrackViewedAt())
                .trackDuration(plexMediaDto.getTrackDuration())
                .trackOriginalTitle(plexMediaDto.getTrackOriginalTitle())
                .trackRatingCount(plexMediaDto.getTrackRatingCount())
                .trackUserRating(plexMediaDto.getTrackUserRating())
                .trackYear(plexMediaDto.getTrackYear())

                .trackFormat(plexMediaDto.getTrackFormat())
                .trackBitrate(plexMediaDto.getTrackBitrate())
                .build();
    }
}
