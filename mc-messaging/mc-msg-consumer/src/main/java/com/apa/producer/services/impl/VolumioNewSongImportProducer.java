package com.apa.producer.services.impl;

import com.apa.client.volumio.VolumioClient;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.executor.impl.VolumioMediaImporter;
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
    private VolumioMediaImporter volumioMediaImporter;
    @Autowired
    private VolumioClient volumioClient;

    public List<VolumioMediaDto> produceNewVolumioTrackMessage() {
        List<VolumioMediaDto> volumioMediaDtos = getNewVolumioTrack();
        return volumioMediaDtos;
    }

    private List<VolumioMediaDto> getNewVolumioTrack() {
        List<VolumioMediaDto> notSavedVolumioLocalAlbum = volumioClient.getNotSavedVolumioLocalAlbum();
        List<VolumioMediaDto> notSavedVolumioTidalAlbum = volumioClient.getNotSavedVolumioTidalAlbum();
        List<VolumioMediaDto> volumioMediaDtos = Stream.concat(notSavedVolumioLocalAlbum.stream(), notSavedVolumioTidalAlbum.stream()).collect(Collectors.toList());
        return volumioMediaDtos;
    }

}
