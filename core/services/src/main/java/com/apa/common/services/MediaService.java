package com.apa.common.services;

import com.apa.common.entities.media.Media;
import com.apa.common.entities.VersionMedia;

public interface MediaService<T extends Media> {
    VersionMedia<T> save(T t);
}
