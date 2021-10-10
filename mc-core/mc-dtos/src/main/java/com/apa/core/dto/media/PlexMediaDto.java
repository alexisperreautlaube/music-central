package com.apa.core.dto.media;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class PlexMediaDto extends MediaDto {

    private String albumName;
    private String artistName;

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
    private String plexId;
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
