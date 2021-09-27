package com.apa.events.mapper;

import com.apa.common.entities.media.TidalMedia;
import com.apa.core.dto.media.TidalMediaDto;

import java.util.UUID;

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
}
