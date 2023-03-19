package com.apa.common.entities.media;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AvailableMedias {
    @Field(targetType = FieldType.STRING)
    private String id;
    private Integer score = 0;
    private Integer playCount;
    private Integer rating;
    private LocalDate releaseDate;
    private List<RelatedMedia> relatedMedias;
}
