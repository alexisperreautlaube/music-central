package com.apa.common.services;

import com.apa.common.entities.Media;

public interface MediaService<T extends Media> {
    T save(T t);
}
