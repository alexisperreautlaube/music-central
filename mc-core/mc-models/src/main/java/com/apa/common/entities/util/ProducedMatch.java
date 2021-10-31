package com.apa.common.entities.util;

import lombok.*;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProducedMatch {
    private MediaReference from;
    private MediaReference to;
}
