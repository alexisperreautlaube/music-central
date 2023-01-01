package com.apa.common.mapper;

import com.apa.common.entities.media.TidalMedia;
import com.apa.core.dto.media.TidalMediaDto;

public class TidalMediaMapper {
    public static TidalMedia toTidalMedia(TidalMediaDto tidalMediaDto) {
        return TidalMedia.builder()
                .artistId(tidalMediaDto.getArtistId())
                .albumId(tidalMediaDto.getAlbumId())
                .artists(tidalMediaDto.getArtists())
                .trackCount(tidalMediaDto.getTrackCount())
                .discCount(tidalMediaDto.getDiscCount())
                .albumDuration(tidalMediaDto.getAlbumDuration())
                .releaseDate(tidalMediaDto.getReleaseDate())
                .trackDuration(tidalMediaDto.getTrackDuration())
                .trackNumber(tidalMediaDto.getTrackNumber())
                .discNumber(tidalMediaDto.getDiscNumber())
                .trackVersion(tidalMediaDto.getTrackVersion())
                .popularity(tidalMediaDto.getPopularity())
                .available(tidalMediaDto.isAvailable())
                .type(tidalMediaDto.getType())
                .tidalTrackId(tidalMediaDto.getTidalTrackId())
                .artistName(tidalMediaDto.getArtistName())
                .albumName(tidalMediaDto.getAlbumName())
                .trackTitle(tidalMediaDto.getTrackTitle())
                .build();
    }

    public static TidalMediaDto toTidalMediaDto(TidalMedia tidalMedia) {
        return TidalMediaDto.builder()
                .artistId(tidalMedia.getArtistId())
                .albumId(tidalMedia.getAlbumId())
                .artists(tidalMedia.getArtists())
                .trackCount(tidalMedia.getTrackCount())
                .discCount(tidalMedia.getDiscCount())
                .albumDuration(tidalMedia.getAlbumDuration())
                .releaseDate(tidalMedia.getReleaseDate())
                .trackDuration(tidalMedia.getTrackDuration())
                .trackNumber(tidalMedia.getTrackNumber())
                .discNumber(tidalMedia.getDiscNumber())
                .trackVersion(tidalMedia.getTrackVersion())
                .popularity(tidalMedia.getPopularity())
                .available(tidalMedia.isAvailable())
                .type(tidalMedia.getType())
                .tidalTrackId(tidalMedia.getTidalTrackId())
                .artistName(tidalMedia.getArtistName())
                .albumName(tidalMedia.getAlbumName())
                .trackTitle(tidalMedia.getTrackTitle())
                .build();
    }
}
