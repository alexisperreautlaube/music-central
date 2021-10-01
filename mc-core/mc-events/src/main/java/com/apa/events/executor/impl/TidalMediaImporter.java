package com.apa.events.executor.impl;

import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.impl.TidalMediaService;
import com.apa.core.dto.media.TidalMediaDto;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.TidalMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TidalMediaImporter extends EventExecutor<TidalMediaDto> {

    @Autowired
    private TidalMediaService tidalMediaService;

    @Override
    protected void doExecute(MusicCentralEvent e, TidalMediaDto tidalMediaDto) {
        TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
        tidalMediaService.getByTidalTrackId(tidalMediaDto.getTidalTrackId()).ifPresent(m -> tidalMedia.setUuid(m.getUuid()));
        tidalMediaService.save(tidalMedia);
    }

    @Override
    protected boolean existAndEquals(TidalMediaDto tidalMediaDto) {
        TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
        return tidalMediaService.existAndEquals(tidalMedia);
    }
}
