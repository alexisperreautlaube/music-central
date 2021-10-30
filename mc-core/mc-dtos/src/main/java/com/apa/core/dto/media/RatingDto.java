package com.apa.core.dto.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    private String trackId;
    private Integer rating = 0;
    private LocalDateTime rateDate;
}
