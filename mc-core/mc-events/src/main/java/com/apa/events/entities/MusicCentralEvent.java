package com.apa.events.entities;

import com.apa.events.entities.enums.MusicCentralEventStates;
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

    private String executorClassName;

    private String mediaClassName;
}
