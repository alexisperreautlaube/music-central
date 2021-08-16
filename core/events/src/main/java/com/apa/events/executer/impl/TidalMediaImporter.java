package com.apa.events.executer.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.impl.TidalMediaService;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executer.EventExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TidalMediaImporter extends EventExecutor<TidalMedia> {

    @Autowired
    private TidalMediaService tidalMediaService;

    @Override
    protected List<EventAudit> doExecute(MusicCentralEvent e, TidalMedia localMedia) {
        VersionMedia<TidalMedia> save = tidalMediaService.save(localMedia);
        return List.of(new EventAudit(save.getMedia().getUuid(), save.getMedia().getClass(), save.getVersion()));
    }
}
