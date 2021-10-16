package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;

import java.util.List;

public interface MediaService<T extends MusicCentralMedia> {
    T save(T t);
    List<T> findPerfectMatch(String artistName, String albumName, String trackTitle, String trackIndex);
    boolean existAndEquals(T media);
}
