package com.apa.common.services;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.LocalMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.services.impl.LocalMediaService;
import com.apa.common.services.impl.PlexMediaService;
import com.apa.common.services.impl.TidalMediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MusicCentralMediaServiceProviderIT extends AbstractCommonIT {

    @Autowired
    private MediaServiceProvider mediaServiceProvider;

    @Test
    public void testLocalMedia() {
        Optional<AbstractMediaService> mediaService = mediaServiceProvider.provideMediaService(LocalMedia.class);
        assertTrue(mediaService.get().getClass().equals(LocalMediaService.class));
    }

    @Test
    public void testPlexMedia() {
        Optional<AbstractMediaService> mediaService = mediaServiceProvider.provideMediaService(PlexMedia.class);
        assertTrue(mediaService.get().getClass().equals(PlexMediaService.class));
    }

    @Test
    public void testTidalMedia() {
        Optional<AbstractMediaService> mediaService = mediaServiceProvider.provideMediaService(TidalMedia.class);
        assertTrue(mediaService.get().getClass().equals(TidalMediaService.class));
    }

    @Test
    public void testNoneExistingMedia() {
        Optional<AbstractMediaService> mediaService = mediaServiceProvider.provideMediaService(Exception.class);
        assertTrue(mediaService.isEmpty());
    }
}