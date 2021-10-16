package com.apa.common.services;

import com.apa.common.entities.media.MusicCentralMedia;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractMediaService<E extends MusicCentralMedia> {

    @Getter
    private final Class<E> persistentClass;

    public AbstractMediaService() {
        this.persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public abstract List<E> findAll();

    public abstract boolean exist(E media);
}
