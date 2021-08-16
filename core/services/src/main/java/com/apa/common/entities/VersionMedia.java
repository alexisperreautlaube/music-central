package com.apa.common.entities;

import com.apa.common.entities.media.MusicCentralMedia;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VersionMedia<E extends MusicCentralMedia> {
    private long version;
    private E media;
}
