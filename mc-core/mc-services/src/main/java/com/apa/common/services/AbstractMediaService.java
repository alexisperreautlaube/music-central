package com.apa.common.services;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.services.media.MediaService;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractMediaService<E extends MusicCentralMedia> implements MediaService<E> {

    @Getter
    private final Class<E> persistentClass;

    public AbstractMediaService() {
        this.persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public abstract E save(E media);

    public abstract boolean existAndEquals(E media);
}
