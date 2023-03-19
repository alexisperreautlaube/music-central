package com.apa.client.apple.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AppleTrack {

    @Id
    private Long id;
    private String clazz;
    private Short index;
    private String name;
    private String persistent;
    private Long database;
    private LocalDateTime dateAdded;
    private Duration time;
    private Double duration;
    private String artist;
    private String albumArtist;
    private String composer;
    private String album;
    private String genre;
    private Integer bitRate;
    private Integer sampleRate;
    private Short trackCount;
    private Short trackNumber;
    private Short discCount;
    private Short discNumber;
    private Integer size;
    private Integer volumeAdjustment;
    private Short year;
    private String comment;
    private String eq;
    private String kind;
    private String mediaKind;
    private LocalDateTime modificationDate;
    private Boolean enabled;
    private Double start;
    private Double finish;
    private Integer playedCount;
    private LocalDateTime playedDate;
    private Integer skippedCount;
    private LocalDateTime skippedDate;
    private Boolean compilation;
    private Short rating;
    private Integer bpm;
    private String grouping;
    private Boolean bookmarkable;
    private Double bookmark;
    private Boolean shufflable;
    private String lyrics;
    private String category;
    private String description;
    private Short episodeNumber;
    private Boolean unplayed;
    private String sortName;
    private String sortAlbum;
    private String sortArtist;
    private String sortComposer;
    private String sortAlbumArtist;
    private LocalDateTime releaseDate;
    private Boolean loved;
    private Boolean disliked;
    private Boolean albumLoved;
    private Boolean albumDisliked;
    private String cloudStatus;
    private String work;
    private String movement;
    private Integer movementNumber;
    private Short movementCount;
}
