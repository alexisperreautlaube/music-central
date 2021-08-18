package com.apa.events.mapper;

import com.apa.common.entities.media.PlexMedia;
import com.apa.core.dto.media.PlexMediaDto;

import java.util.UUID;

public class PlexMediaMapper {
    public static PlexMedia toPlexMedia(PlexMediaDto plexMediaDto) {
        return PlexMedia.builder()
                .uuid(UUID.randomUUID())
                .plexId(plexMediaDto.getPlexId())
                .artist(plexMediaDto.getArtist())
                .album(plexMediaDto.getAlbum())
                .title(plexMediaDto.getTitle())
                .build();
    }
}
