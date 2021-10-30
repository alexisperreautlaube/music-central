package com.apa.core.dto.media;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
public class VolumioMediaDto extends MediaDto {

    private String albumTitle;
    private String albumUri;
    private String albumTrackType;
    private String albumArtist;
    private String albumAudioQuality;
    private String trackType;
    private String trackArtist;
    private String trackUri;
    private String trackDuration;
    private String trackNumber;
    private String trackAudioQuality;
    private LocalDate addedDate;

}
