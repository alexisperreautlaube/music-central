package com.apa.common.entities.media;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TidalMedia extends MusicCentralMedia {
    private String artistId;
    private Map<String, String> artists;
    private String albumId;
    private int trackCount;
    private int discCount;
    private long albumDuration;
    private LocalDate release_date;
    private String tidalTrackId;
    private long trackDuration;
    private int trackNumber;
    private int discNumber;
    private String trackVersion;
    private int popularity;
    private boolean available;
    private String type;
}
