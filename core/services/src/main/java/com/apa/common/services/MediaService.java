package com.apa.common.services;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.entities.VersionMedia;

import java.util.UUID;

public interface MediaService<T extends MusicCentralMedia> {
    VersionMedia<T> save(T t);

    void delete(UUID uuit);
}
