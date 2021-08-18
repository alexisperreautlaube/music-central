package com.apa.events.mapper;

import com.apa.common.entities.media.LocalMedia;
import com.apa.core.dto.media.LocalMediaDto;

import java.util.UUID;

public class LocalMediaMapper {
    public static LocalMedia toLocalMedia(LocalMediaDto localMediaDto) {
        return LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(localMediaDto.getLocalId())
                .artist(localMediaDto.getArtist())
                .album(localMediaDto.getAlbum())
                .title(localMediaDto.getTitle())
                .build();
    }
}
