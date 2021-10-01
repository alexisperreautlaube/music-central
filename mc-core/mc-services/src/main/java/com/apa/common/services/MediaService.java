package com.apa.common.services;

import com.apa.common.entities.media.MusicCentralMedia;

public interface MediaService<T extends MusicCentralMedia> {
    T save(T t);

    void delete(String uuid);
}
