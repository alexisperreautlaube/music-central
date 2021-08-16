package com.apa.common.entities.media;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class PlexMedia extends MusicCentralMedia {
    private String plexId;
}
