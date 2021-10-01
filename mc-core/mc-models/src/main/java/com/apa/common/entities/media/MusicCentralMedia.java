package com.apa.common.entities.media;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
public class MusicCentralMedia {
    private String trackTitle;
    private String albumName;
    private String artistName;
}
