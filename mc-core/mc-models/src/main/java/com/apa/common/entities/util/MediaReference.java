package com.apa.common.entities.util;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MediaReference {
    private String clazz;
    @Field(targetType = FieldType.STRING)
    private String id;
}
