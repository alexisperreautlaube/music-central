package com.apa.events.executer;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.services.AbstractMediaService;
import com.apa.common.services.MediaServiceProvider;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.entities.enums.MusicCentralEventStates;
import com.apa.events.repositories.MusicCentralEventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class EventExecutor<M extends MusicCentralMedia> {
    
    @Autowired
    private MusicCentralEventRepository musicCentralEventRepository;

    @Autowired
    private MediaServiceProvider mediaServiceProvider;

    private Class<EventExecutor> executerClass;

    private Class<MusicCentralEvent> musicCentralEventClass;

    private Class<MusicCentralMedia> musicCentralMediaClass;

    public EventExecutor() {
        this.executerClass = (Class<EventExecutor>) this.getClass();
        this.musicCentralEventClass = (Class<MusicCentralEvent>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.musicCentralMediaClass = (Class<MusicCentralMedia>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public MusicCentralEvent execute(M m) {
        MusicCentralEvent e = new MusicCentralEvent();
        e.setExecuterClass(executerClass);
        e.setMediaClass(musicCentralMediaClass);
        try {
            doExecute(e, m);
        } catch (Throwable t) {
            e.setState(MusicCentralEventStates.EXECUTE_FAILED);
            throw t;
        }
        e.setState(MusicCentralEventStates.EXECUTED);
        e.setDateExecuted(LocalDateTime.now());
        musicCentralEventRepository.save(e);
        return e;
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

    private void doRollback(MusicCentralEvent e) {
        e.getEventAudits().forEach(eventAudit -> {
            Optional<AbstractMediaService> mediaService = mediaServiceProvider.provideMediaService(eventAudit.getClazz());
            mediaService.get().restore(eventAudit.getUuid(), eventAudit.getVersion() - 1);
        });
    }
}
