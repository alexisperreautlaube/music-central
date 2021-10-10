package com.apa.events.executor;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.core.dto.media.MediaDto;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.entities.enums.MusicCentralEventStates;
import com.apa.events.repositories.MusicCentralEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public abstract class EventExecutor<M extends MediaDto> {
    
    @Autowired
    private MusicCentralEventRepository musicCentralEventRepository;

    private Class<EventExecutor> executerClass;

    private Class<MusicCentralMedia> musicCentralMediaClass;

    public EventExecutor() {
        this.executerClass = (Class<EventExecutor>) this.getClass();
        this.musicCentralMediaClass = (Class<MusicCentralMedia>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Optional<MusicCentralEvent> execute(M m) {
        if (existAndEquals(m)) {
            return Optional.empty();
        }
        MusicCentralEvent e = new MusicCentralEvent();
        e.setExecutorClassName(executerClass.getName());
        e.setMediaClassName(musicCentralMediaClass.getName());
        try {
            doExecute(e, m);
        } catch (Throwable t) {
            e.setState(MusicCentralEventStates.EXECUTE_FAILED);
            log.info("executor={} failed, {} - {} - {}", e.getExecutorClassName(), m.getTrackTitle());
            musicCentralEventRepository.save(e);
            throw t;
        }
        e.setState(MusicCentralEventStates.EXECUTED);
        e.setDateExecuted(LocalDateTime.now());
        musicCentralEventRepository.save(e);
        return Optional.of(e);
    }


    protected abstract void doExecute(MusicCentralEvent e, M m);

    protected abstract boolean existAndEquals(M m);

}
