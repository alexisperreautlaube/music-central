package com.apa.events.executor.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.services.impl.LocalMediaService;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalMediaImporter extends EventExecutor<LocalMedia> {

    @Autowired
    private LocalMediaService localMediaService;

    @Override
    protected List<EventAudit> doExecute(MusicCentralEvent e, LocalMedia localMedia) {
        VersionMedia<LocalMedia> save = localMediaService.save(localMedia);
        return List.of(new EventAudit(save.getMedia().getUuid(), save.getMedia().getClass().getName(), save.getVersion()));
    }
}
