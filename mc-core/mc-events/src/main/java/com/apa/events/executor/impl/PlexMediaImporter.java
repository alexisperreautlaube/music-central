package com.apa.events.executor.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.core.dto.media.PlexMediaDto;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.PlexMediaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlexMediaImporter extends EventExecutor<PlexMediaDto> {

    @Autowired
    private PlexMediaService plexMediaService;

    @Override
    protected void doExecute(PlexMediaDto plexMediaDto) {
        PlexMedia plexMedia = PlexMediaMapper.toPlexMedia(plexMediaDto);
        plexMediaService.save(plexMedia);
        log.debug("Success, {} - {} - {}", plexMedia.getArtistName(), plexMedia.getAlbumName(), plexMedia.getTrackTitle());
    }

    @Override
    protected boolean existAndEquals(PlexMediaDto plexMediaDto) {
        PlexMedia plexMedia = PlexMediaMapper.toPlexMedia(plexMediaDto);
        return plexMediaService.existAndEquals(plexMedia);
    }
}
