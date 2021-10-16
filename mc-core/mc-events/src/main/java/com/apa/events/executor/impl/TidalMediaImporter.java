package com.apa.events.executor.impl;

import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.core.dto.media.TidalMediaDto;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.TidalMediaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TidalMediaImporter extends EventExecutor<TidalMediaDto> {

    @Autowired
    private TidalMediaService tidalMediaService;

    @Override
    protected void doExecute(MusicCentralEvent e, TidalMediaDto tidalMediaDto) {
        TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
        tidalMediaService.save(tidalMedia);
        log.info("Success, {} - {} - {}", tidalMedia.getArtistName(), tidalMedia.getAlbumName(), tidalMedia.getTrackTitle());
    }

    @Override
    protected boolean existAndEquals(TidalMediaDto tidalMediaDto) {
        TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
        return tidalMediaService.existAndEquals(tidalMedia);
    }
}
