package com.apa.common.entities.media;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.util.Map;

@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TidalMedia extends MusicCentralMedia {
    private String albumName;
    private String artistName;
    private String artistId;
    private Map<String, String> artists;
    private String albumId;
    private int trackCount;
    private int discCount;
    private long albumDuration;
    private LocalDate releaseDate;
    @Id
    @Field(targetType = FieldType.STRING)
    private String tidalTrackId;
    private long trackDuration;
    private int trackNumber;
    private int discNumber;
    private String trackVersion;
    private int popularity;
    private boolean available;
    private String type;
}
