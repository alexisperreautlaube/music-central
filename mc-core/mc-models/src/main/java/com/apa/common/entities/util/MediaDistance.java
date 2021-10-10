package com.apa.common.entities.util;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MediaDistance {
    private MediaReference from;
    private MediaReference to;
    private StringsDistance artist;
    private StringsDistance album;
    private StringsDistance song;
}
