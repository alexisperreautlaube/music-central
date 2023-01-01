package com.apa.common.services.newsongs;

import com.apa.common.AbstractCommonIT;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.media.impl.MediaErrorService;
import com.apa.core.dto.media.VolumioMediaDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

public class VolumioNewSongImportProducerTest extends AbstractCommonIT {

    @Autowired
    private MediaErrorService mediaErrorService;

    @Autowired
    private VolumioMediaRepository volumioMediaRepository;

    @Autowired
    private VolumioNewSongImportProducer volumioNewSongImportProducer;

    @Test
    public void getNewLocalSimple() {
        when(volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus)).thenReturn(List.of(VolumioMediaDto.builder()
                        .addedDate(LocalDate.now())
                        .trackType("flac")
                        .trackUri("uri1")
                .build()));
        Assertions.assertEquals(volumioNewSongImportProducer.getNewVolumioTracks().size(), 1);
    }

    @Test
    public void getNewLocalExistAndEquals() {
        volumioMediaRepository.save(VolumioMedia.builder()
                .trackType("flac")
                .trackUri("uri1")
                .build());
        when(volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus)).thenReturn(List.of(VolumioMediaDto.builder()
                        .trackType("flac")
                        .trackUri("uri1")
                .build()));
        Assertions.assertEquals(volumioNewSongImportProducer.getNewVolumioTracks().size(), 0);
    }

    @Test
    public void getNewLocalExistNotEquals() {
        volumioMediaRepository.save(VolumioMedia.builder()
                .trackType("flac")
                .trackUri("uri1")
                .build());
        when(volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus)).thenReturn(List.of(VolumioMediaDto.builder()
                        .trackType("flac")
                        .albumArtist("patate")
                        .trackUri("uri1")
                .build()));
        Assertions.assertEquals(volumioNewSongImportProducer.getNewVolumioTracks().size(), 1);
    }

    @Test
    public void getNewLocalExclusionTest1() {
        when(volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus)).thenReturn(List.of(VolumioMediaDto.builder()
                        .addedDate(LocalDate.now())
                        .trackType("avi")
                        .trackUri("uri1")
                .build()));
        Assertions.assertEquals(volumioNewSongImportProducer.getNewVolumioTracks().size(), 0);
    }

    @Test
    public void getNewLocalExclusionTest2() {
        when(volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus)).thenReturn(List.of(VolumioMediaDto.builder()
                        .addedDate(LocalDate.now())
                        .trackType("mpg")
                        .trackUri("uri1")
                .build()));
        Assertions.assertEquals(volumioNewSongImportProducer.getNewVolumioTracks().size(), 0);
    }

    @Test
    public void getNewLocalExclusionTest3() {
        when(volumioClient.getVolumioLocalMedias(mediaErrorService::saveDetemineStatus)).thenReturn(List.of(VolumioMediaDto.builder()
                        .addedDate(LocalDate.now())
                        .trackType("mov")
                        .trackUri("uri1")
                .build()));
        Assertions.assertEquals(volumioNewSongImportProducer.getNewVolumioTracks().size(), 0);
    }
}