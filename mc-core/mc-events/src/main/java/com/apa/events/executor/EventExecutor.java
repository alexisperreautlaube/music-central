package com.apa.events.executor;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.core.dto.media.MediaDto;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class EventExecutor<M extends MediaDto> {


    private Class<EventExecutor> executerClass;

    private Class<MusicCentralMedia> musicCentralMediaClass;

    public EventExecutor() {
        this.executerClass = (Class<EventExecutor>) this.getClass();
        this.musicCentralMediaClass = (Class<MusicCentralMedia>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void execute(M m) {
        if (existAndEquals(m)) {
            return;
        }
        doExecute(m);
    }


    protected abstract void doExecute(M m);

    protected abstract boolean existAndEquals(M m);

}
