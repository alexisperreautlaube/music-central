package com.apa.events.entities;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.events.entities.enums.MusicCentralEventStates;
import com.apa.events.executer.EventExecutor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MusicCentralEvent {

    private MusicCentralEventStates state = MusicCentralEventStates.CREATED;

    private LocalDateTime dateCreated = LocalDateTime.now();

    private LocalDateTime dateExecuted;

    private List<EventAudit> eventAudits = new ArrayList<>();

    private Class<EventExecutor> executerClass;

    private Class<MusicCentralMedia> mediaClass;
}
