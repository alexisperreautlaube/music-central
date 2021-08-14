package com.apa.common.entities;

import com.apa.common.entities.media.Media;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VersionMedia<E extends Media> {
    private long version;
    private E media;
}
