package com.apa.common.services;

import com.apa.common.entities.media.Media;
import com.apa.common.entities.VersionMedia;

import java.util.UUID;

public interface MediaService<T extends Media> {
    VersionMedia<T> save(T t);

    void delete(UUID uuit);
}
