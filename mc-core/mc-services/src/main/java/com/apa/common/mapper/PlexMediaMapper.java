package com.apa.common.mapper;

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

    public static PlexMediaDto toPLexDto(PlexMedia plexMedia) {
        return PlexMediaDto.builder()
                .plexId(plexMedia.getPlexId().toString())
                .artistName(plexMedia.getArtistName())
                .albumName(plexMedia.getAlbumName())
                .trackTitle(plexMedia.getTrackTitle())
                .addedAt(plexMedia.getAddedAt())
                .albumIndex(plexMedia.getAlbumIndex())
                .albumLastViewedAt(plexMedia.getAlbumLastViewedAt())
                .albumRatingKey(plexMedia.getAlbumRatingKey())
                .albumSortTitle(plexMedia.getAlbumSortTitle())
                .albumViewCount(plexMedia.getAlbumViewCount())
                .albumOriginallyAvailableAt(plexMedia.getAlbumOriginallyAvailableAt())
                .artistKey(plexMedia.getArtistKey())
                .albumStudio(plexMedia.getAlbumStudio())
                .albumYear(plexMedia.getAlbumYear())

                .trackAddedAt(plexMedia.getTrackAddedAt())
                .trackIndex(plexMedia.getTrackIndex())
                .trackLastViewedAt(plexMedia.getTrackLastViewedAt())
                .trackSortTitle(plexMedia.getTrackSortTitle())
                .trackUpdatedAt(plexMedia.getTrackUpdatedAt())
                .trackViewCount(plexMedia.getTrackViewCount())
                .trackViewedAt(plexMedia.getTrackViewedAt())
                .trackDuration(plexMedia.getTrackDuration())
                .trackOriginalTitle(plexMedia.getTrackOriginalTitle())
                .trackRatingCount(plexMedia.getTrackRatingCount())
                .trackUserRating(plexMedia.getTrackUserRating())
                .trackYear(plexMedia.getTrackYear())

                .trackFormat(plexMedia.getTrackFormat())
                .trackBitrate(plexMedia.getTrackBitrate())
                .build();
    }
}
