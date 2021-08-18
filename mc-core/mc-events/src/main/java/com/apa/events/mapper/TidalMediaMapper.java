package com.apa.events.mapper;

import com.apa.common.entities.media.TidalMedia;
import com.apa.core.dto.media.TidalMediaDto;

import java.util.UUID;

public class TidalMediaMapper {
    public static TidalMedia toTidalMedia(TidalMediaDto tidalMediaDto) {
        return TidalMedia.builder()
                .uuid(UUID.randomUUID())
                .tidalId(tidalMediaDto.getTidalId())
                .artist(tidalMediaDto.getArtist())
                .album(tidalMediaDto.getAlbum())
                .title(tidalMediaDto.getTitle())
                .build();
    }
}
