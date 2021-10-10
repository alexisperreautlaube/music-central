package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;

public interface MediaService<T extends MusicCentralMedia> {
    T save(T t);
}
