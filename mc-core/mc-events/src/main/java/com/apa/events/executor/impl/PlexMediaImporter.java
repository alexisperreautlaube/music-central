package com.apa.events.executor.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.services.impl.PlexMediaService;
import com.apa.core.dto.media.PlexMediaDto;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.PlexMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlexMediaImporter extends EventExecutor<PlexMediaDto> {

    @Autowired
    private PlexMediaService plexMediaService;

    @Override
    protected List<EventAudit> doExecute(MusicCentralEvent e, PlexMediaDto plexMediaDto) {
        PlexMedia plexMedia = PlexMediaMapper.toPlexMedia(plexMediaDto);
        VersionMedia<PlexMedia> save = plexMediaService.save(plexMedia);
        return List.of(new EventAudit(save.getMedia().getUuid(), save.getMedia().getClass().getName(), save.getVersion()));
    }
}
