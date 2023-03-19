package com.apa.common.entities.media;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlexMedia extends MusicCentralMedia {
    private String albumName;
    private String artistName;
    @Id
    @Field(targetType = FieldType.STRING)
    private String plexId;
    private LocalDateTime addedAt;
    private String albumIndex;

    private LocalDateTime albumLastViewedAt;
    private String albumRatingKey;
    private String albumSortTitle;
    private Integer albumViewCount;
    private LocalDate albumOriginallyAvailableAt;
    private String artistKey;
    private String albumStudio;
    private Integer albumYear;

    private LocalDateTime trackAddedAt;
    private String trackIndex;
    private LocalDateTime trackLastViewedAt;
    private String trackSortTitle;
    private LocalDateTime trackUpdatedAt;
    private Integer trackViewCount;
    private LocalDateTime trackViewedAt;
    private Integer trackDuration;
    private String trackOriginalTitle;
    private Integer trackRatingCount;
    private Double trackUserRating;
    private Integer trackYear;

    private String trackFormat;
    private Integer trackBitrate;
}
