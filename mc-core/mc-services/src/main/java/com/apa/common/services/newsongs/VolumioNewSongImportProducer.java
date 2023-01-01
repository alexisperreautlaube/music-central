package com.apa.common.services.newsongs;

import com.apa.client.volumio.VolumioClient;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.core.dto.media.VolumioMediaDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class VolumioNewSongImportProducer {

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private VolumioClient volumioClient;

    public List<VolumioMediaDto> getNewVolumioTracks() {
        List<VolumioMediaDto> volumioMediaDtos = getNewVolumioTrack();
        return volumioMediaDtos;
    }

    private List<VolumioMediaDto> getNewVolumioTrack() {
        List<VolumioMediaDto> notSavedVolumioLocalAlbum = volumioMediaService.getNotSavedVolumioLocalAlbum();
        List<VolumioMediaDto> notSavedVolumioTidalAlbum = volumioMediaService.getNotSavedVolumioTidalAlbum();
        List<VolumioMediaDto> volumioMediaDtos = Stream.concat(notSavedVolumioLocalAlbum.stream(), notSavedVolumioTidalAlbum.stream()).collect(Collectors.toList());
        return volumioMediaDtos;
    }

}
