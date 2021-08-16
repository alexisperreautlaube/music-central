package com.apa.common.entities.media;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TidalMedia extends MusicCentralMedia {
    private String tidalId;
}