package com.apa.client.apple.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AppleAvailableTrack {
    private Long id;
    @Setter
    private Integer weight = 0;
    private LocalDateTime releaseDate;
    private Short rating;
    private Integer playedCount;
    private String artist;
    private String album;
    private String track;
}
