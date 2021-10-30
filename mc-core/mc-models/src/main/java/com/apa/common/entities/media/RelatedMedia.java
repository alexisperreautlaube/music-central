package com.apa.common.entities.media;

import com.apa.common.entities.enums.MediaQuality;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RelatedMedia {

    @Getter
    @Builder
    public static class RelatedMediaRating {
        private Integer rating = 0;
        private LocalDateTime rateDate;
    }

    @Field(targetType = FieldType.STRING)
    private String id;
    private String clazz;
    private Integer playCount;
    private MediaQuality quality;
    private RelatedMedia.RelatedMediaRating rating;
    private LocalDateTime addedDate;
}