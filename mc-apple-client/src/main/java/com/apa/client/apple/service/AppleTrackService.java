package com.apa.client.apple.service;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.repository.AppleTrackRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AppleTrackService {

    public static final Short MINIMAL_RATING = Short.valueOf("80");
    public static final Short NONE_RATING = Short.valueOf("0");
    @Autowired
    private AppleTrackRepository appleTrackRepository;

    @Autowired
    private AppleClient appleClient;

    public void saveAllTracks(){
        List<Integer> allTracks = appleClient.getAllTracksIds();
        int total = allTracks.size();
        AtomicInteger rendu = new AtomicInteger(1);
        allTracks.forEach( t ->{
            if (!appleTrackRepository.existsById(Long.valueOf(t.longValue()))){
                AppleTrack appleTrackById = appleClient.getAppleTrackById(t);
                log.info("Saving Artist={}, Album={}, Track={}, {}/{}", appleTrackById.getArtist(), appleTrackById.getAlbum(), appleTrackById.getName(), rendu.get(), total);
                appleTrackRepository.save(appleTrackById);
            }
            rendu.getAndIncrement();
        });
    }

    public void saveAllTracksByList(){
        List<Integer> allTracks = appleClient.getAllTracksIds();
        List<List<Integer>> subSets = Lists.partition(allTracks, 300);
        int total = allTracks.size();
        int totalPage = subSets.size();
        AtomicInteger rendu = new AtomicInteger(1);
        AtomicInteger renduPage = new AtomicInteger(0);
        subSets.parallelStream().forEach( t ->{
            renduPage.getAndIncrement();
            log.info("Page {}/{}", renduPage.get(), totalPage);
            List<AppleTrack> appleTrackByIds = appleClient.getAppleTrackByIds(t);
            appleTrackByIds.parallelStream().forEach(a -> {
                Optional<AppleTrack> byId = appleTrackRepository.findById(a.getId());
                if (!byId.isPresent() || byId.get().getModificationDate().isAfter(a.getModificationDate())){
                    log.info("Saving Artist={}, Album={}, Track={}, {}/{}", a.getArtist(), a.getAlbum(), a.getName(), rendu.get(), total);
                    appleTrackRepository.save(a);
                }
                rendu.getAndIncrement();
            });
        });
    }

    public List<AppleTrack> findAll() {
        return appleTrackRepository.findAll();
    }

    public List<AppleTrack> findBestOf() {
        return appleTrackRepository.findAppleTrackByRatingGreaterThanEqual(MINIMAL_RATING);
    }

    public List<AppleTrack> findNoneRated() {
        return appleTrackRepository.findAppleTrackByRatingEquals(NONE_RATING);
    }
}
