package com.apa.events.mapper;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.core.dto.media.VolumioMediaDto;

public class VolumioMediaMapper {
    public static VolumioMedia toVolumioMedia(VolumioMediaDto volumioMediaDto) {
        return VolumioMedia.builder()
                .trackUri(volumioMediaDto.getTrackUri())
                .trackTitle(volumioMediaDto.getTrackTitle())
                .albumTitle(volumioMediaDto.getAlbumTitle())
                .albumUri(volumioMediaDto.getAlbumUri())
                .albumTrackType(volumioMediaDto.getAlbumTrackType())
                .albumArtist(volumioMediaDto.getAlbumArtist())
                .albumAudioQuality(volumioMediaDto.getAlbumAudioQuality())
                .trackType(volumioMediaDto.getTrackType())
                .trackArtist(volumioMediaDto.getTrackArtist())
                .trackDuration(volumioMediaDto.getTrackDuration())
                .trackNumber(volumioMediaDto.getTrackNumber())
                .trackAudioQuality(volumioMediaDto.getTrackAudioQuality())
                .build();
    }
}
