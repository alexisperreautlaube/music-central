package com.apa.common.entities.media;

import com.apa.common.entities.enums.RatingType;
import com.apa.common.entities.util.MediaReference;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Rating {
    private MediaReference mediaReference;
    private Integer rating = 0;
    private LocalDateTime rateDate;
    private RatingType ratingType;
}
