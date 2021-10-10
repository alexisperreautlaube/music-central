package com.apa.events.executor.impl;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.VolumioMediaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VolumioMediaImporter extends EventExecutor<VolumioMediaDto> {

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Override
    protected void doExecute(MusicCentralEvent e, VolumioMediaDto volumioMediaDto) {
        VolumioMedia volumioMedia = VolumioMediaMapper.toVolumioMedia(volumioMediaDto);
        volumioMediaService.save(volumioMedia);
        log.info("Success, {} - {} - {}", volumioMedia.getTrackArtist(), volumioMedia.getAlbumTitle(), volumioMedia.getTrackTitle());
    }

    @Override
    protected boolean existAndEquals(VolumioMediaDto volumioMediaDto) {
        VolumioMedia volumioMedia = VolumioMediaMapper.toVolumioMedia(volumioMediaDto);
        return volumioMediaService.existAndEquals(volumioMedia);
    }
}