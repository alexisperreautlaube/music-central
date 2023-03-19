package com.apa.common.entities.media;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TmpVolumioMedia extends MusicCentralMedia {
    @Id
    @Field(targetType = FieldType.STRING)
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
    private LocalDate albumReleaseDate;
    private LocalDate addedDate;
}
