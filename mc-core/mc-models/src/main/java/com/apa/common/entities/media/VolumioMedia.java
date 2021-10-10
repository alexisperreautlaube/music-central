package com.apa.common.entities.media;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VolumioMedia extends MusicCentralMedia {
    @Id
    private String trackUri;
    private String albumTitle;
    private String albumUri;
    private String albumTrackType;
    private String albumArtist;
    private String albumAudioQuality;
    private String trackType;
    private String trackArtist;
    private String trackDuration;
    private String trackNumber;
    private String trackAudioQuality;
}
