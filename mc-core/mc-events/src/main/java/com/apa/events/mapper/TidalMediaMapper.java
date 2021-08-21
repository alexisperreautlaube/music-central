package com.apa.events.mapper;

import com.apa.common.entities.media.TidalMedia;
import com.apa.core.dto.media.TidalMediaDto;

import java.util.UUID;

public class TidalMediaMapper {
    public static TidalMedia toTidalMedia(TidalMediaDto tidalMediaDto) {
        return TidalMedia.builder()
                .uuid(UUID.randomUUID())
                .tidalTrackId(tidalMediaDto.getTidalTrackId())
                .artistName(tidalMediaDto.getArtistName())
                .albumName(tidalMediaDto.getAlbumName())
                .trackTitle(tidalMediaDto.getTrackTitle())
                .build();
    }
}
