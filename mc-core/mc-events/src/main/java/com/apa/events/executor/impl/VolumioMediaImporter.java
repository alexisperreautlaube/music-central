package com.apa.events.executor.impl;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.VolumioMediaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class VolumioMediaImporter extends EventExecutor<VolumioMediaDto> {

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Override
    protected void doExecute(VolumioMediaDto volumioMediaDto) {
        VolumioMedia volumioMedia = VolumioMediaMapper.toVolumioMedia(volumioMediaDto);
        boolean log;
        try {
            log = volumioMediaService.findById(volumioMedia.getTrackUri()) == null;
        } catch (Throwable throwable) {
            log = true;
        }
        if (log) {
            VolumioMediaImporter.log.info("Success, {} - {} - {}", volumioMedia.getTrackArtist(), volumioMedia.getAlbumTitle(), volumioMedia.getTrackTitle());
            volumioMedia.setAddedDate(LocalDate.now());
        }
        volumioMediaService.save(volumioMedia);
    }

    @Override
    protected boolean existAndEquals(VolumioMediaDto volumioMediaDto) {
        VolumioMedia volumioMedia = VolumioMediaMapper.toVolumioMedia(volumioMediaDto);
        return volumioMediaService.existAndEquals(volumioMedia);
    }
}
