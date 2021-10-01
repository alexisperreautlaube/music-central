package com.apa.events.mapper;

import com.apa.common.entities.media.LocalMedia;
import com.apa.core.dto.media.LocalMediaDto;

import java.util.UUID;

public class LocalMediaMapper {
    public static LocalMedia toLocalMedia(LocalMediaDto localMediaDto) {
        return LocalMedia.builder()
                .localId(localMediaDto.getLocalId())
                .artistName(localMediaDto.getArtistName())
                .albumName(localMediaDto.getAlbumName())
                .trackTitle(localMediaDto.getTrackTitle())
                .build();
    }
}