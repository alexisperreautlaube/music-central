package com.apa.events.executor;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.MediaServiceProvider;
import com.apa.core.dto.media.MediaDto;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.entities.enums.MusicCentralEventStates;
import com.apa.events.repositories.MusicCentralEventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class EventExecutor<M extends MediaDto> {
    
    @Autowired
    private MusicCentralEventRepository musicCentralEventRepository;

    @Autowired
    private MediaServiceProvider mediaServiceProvider;

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
            e.setEventAudits(doExecute(e, m));
        } catch (Throwable t) {
            e.setState(MusicCentralEventStates.EXECUTE_FAILED);
            throw t;
        }
        e.setState(MusicCentralEventStates.EXECUTED);
        e.setDateExecuted(LocalDateTime.now());
        musicCentralEventRepository.save(e);
        return Optional.of(e);
    }

    public void rollback(MusicCentralEvent e) {
        try {
            doRollback(e);
        } catch (Throwable t) {
            e.setState(MusicCentralEventStates.ROLLEDBACK_FAILED);
            throw t;
        }
        e.setState(e.getState().equals(MusicCentralEventStates.TO_PERMANENTLY_ROLLBACK) ? MusicCentralEventStates.PERMANENTLY_ROLLEDBACK : MusicCentralEventStates.ROLLEDBACK);
        musicCentralEventRepository.save(e);
    }

    protected abstract List<EventAudit> doExecute(MusicCentralEvent e, M m);

    protected abstract boolean existAndEquals(M m);

    private void doRollback(MusicCentralEvent e) {
        e.getEventAudits().forEach(eventAudit -> {
            Optional<AbstractMediaService> mediaService = null;

            mediaService = getMediaService(eventAudit.getEventClassName());

            mediaService.map(s -> s.restore(eventAudit.getUuid(), eventAudit.getVersion() - 1)).orElseThrow();
        });
    }

    private Optional<AbstractMediaService> getMediaService(String eventAudit) {
        try {
            return mediaServiceProvider.provideMediaService(Class.forName(eventAudit));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
