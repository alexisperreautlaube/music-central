package com.apa.events.executor.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.impl.TidalMediaService;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TidalMediaImporter extends EventExecutor<TidalMedia> {

    @Autowired
    private TidalMediaService tidalMediaService;

    @Override
    protected List<EventAudit> doExecute(MusicCentralEvent e, TidalMedia localMedia) {
        VersionMedia<TidalMedia> save = tidalMediaService.save(localMedia);
        return List.of(new EventAudit(save.getMedia().getUuid(), save.getMedia().getClass().getName(), save.getVersion()));
    }
}
