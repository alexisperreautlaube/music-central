package com.apa.events.executor.impl;

import com.apa.common.entities.media.LocalMedia;
import com.apa.common.services.impl.LocalMediaService;
import com.apa.core.dto.media.LocalMediaDto;
import com.apa.events.entities.MusicCentralEvent;
import com.apa.events.executor.EventExecutor;
import com.apa.events.mapper.LocalMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalMediaImporter extends EventExecutor<LocalMediaDto> {

    @Autowired
    private LocalMediaService localMediaService;

    @Override
    protected void doExecute(MusicCentralEvent e, LocalMediaDto localMediaDto) {
        LocalMedia localMedia = LocalMediaMapper.toLocalMedia(localMediaDto);
        localMediaService.save(localMedia);
    }

    @Override
    protected boolean existAndEquals(LocalMediaDto localMediaDto) {
        LocalMedia localMedia = LocalMediaMapper.toLocalMedia(localMediaDto);
        return localMediaService.existAndEquals(localMedia);
    }
}
