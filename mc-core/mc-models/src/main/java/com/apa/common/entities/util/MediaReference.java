package com.apa.common.entities.util;

import lombok.*;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MediaReference {
    private Class clazz;
    private String id;
}
