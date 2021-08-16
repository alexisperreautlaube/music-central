package com.apa.events.executer.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.services.impl.PlexMediaService;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executer.EventExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlexMediaImporter extends EventExecutor<PlexMedia> {

    @Autowired
    private PlexMediaService plexMediaService;

    @Override
    protected List<EventAudit> doExecute(MusicCentralEvent e, PlexMedia plexMedia) {
        VersionMedia<PlexMedia> save = plexMediaService.save(plexMedia);
        return List.of(new EventAudit(save.getMedia().getUuid(), save.getMedia().getClass(), save.getVersion()));
    }
}
