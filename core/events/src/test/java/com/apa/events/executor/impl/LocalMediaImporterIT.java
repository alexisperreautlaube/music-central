package com.apa.events.executor.impl;

import com.apa.common.entities.media.LocalMedia;
import com.apa.common.repositories.LocalMediaRepository;
import com.apa.events.AbstractEventIT;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.entities.enums.MusicCentralEventStates;
import com.apa.events.repositories.MusicCentralEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalMediaImporterIT extends AbstractEventIT {

    @Autowired
    private LocalMediaImporter localMediaImporter;

    @Autowired
    private LocalMediaRepository localMediaRepository;

    @Autowired
    private MusicCentralEventRepository musicCentralEventRepository;

    @BeforeEach
    public void setup() {
        localMediaRepository.deleteAll();
        musicCentralEventRepository.deleteAll();
    }

    @Test
    public void simpleTest() {
        LocalMedia localMedia = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId("localId")
                .artist("artist")
                .album("album")
                .title("title")
                .build();
        MusicCentralEvent execute = localMediaImporter.execute(localMedia);

        // media
        List<LocalMedia> all = localMediaRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(localMedia, all.get(0));

        // event
        MusicCentralEvent musicCentralEvent = musicCentralEventRepository.findAll().get(0);
        assertEquals(MusicCentralEventStates.EXECUTED, musicCentralEvent.getState());
        assertEquals(LocalMediaImporter.class.getName(), musicCentralEvent.getExecutorClassName());
        assertEquals(LocalMedia.class.getName(), musicCentralEvent.getMediaClassName());
        assertNotNull(musicCentralEvent.getDateCreated());
        assertNotNull(musicCentralEvent.getDateExecuted());
        List<EventAudit> eventAudits = musicCentralEvent.getEventAudits();
        assertEquals(1, eventAudits.size());
        EventAudit eventAudit = eventAudits.get(0);
        assertEquals(localMedia.getUuid(), eventAudit.getUuid());
        assertEquals(LocalMedia.class.getName(), eventAudit.getEventClassName());
        assertEquals(1, eventAudit.getVersion());
    }
}