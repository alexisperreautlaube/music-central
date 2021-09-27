package com.apa.events.executor.impl;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.impl.TidalMediaService;
import com.apa.core.dto.media.TidalMediaDto;
import com.apa.events.entities.EventAudit;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.TidalMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TidalMediaImporter extends EventExecutor<TidalMediaDto> {

    @Autowired
    private TidalMediaService tidalMediaService;

    @Override
    protected List<EventAudit> doExecute(MusicCentralEvent e, TidalMediaDto tidalMediaDto) {
        TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
        tidalMediaService.getByTidalTrackId(tidalMediaDto.getTidalTrackId()).ifPresent(m -> tidalMedia.setUuid(m.getUuid()));
        VersionMedia<TidalMedia> save = tidalMediaService.save(tidalMedia);
        return List.of(new EventAudit(save.getMedia().getUuid(), save.getMedia().getClass().getName(), save.getVersion()));
    }

    @Override
    protected boolean existAndEquals(TidalMediaDto tidalMediaDto) {
        TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
        return tidalMediaService.existAndEquals(tidalMedia);
    }
}
