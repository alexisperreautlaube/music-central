package com.apa.client.apple.service;

import com.apa.client.apple.AppleClient;
import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.repository.AppleTrackRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AppleTrackService {

    public static final Short TWO_STARS = Short.valueOf("40");
    public static final Short THREE_STARS = Short.valueOf("60");
    public static final Short FOUR_STARS = Short.valueOf("80");
    public static final Short NONE_RATING = Short.valueOf("0");
    @Autowired
    private AppleTrackRepository appleTrackRepository;

    @Autowired
    private AppleClient appleClient;

    public void saveAllTracksByList(){
        appleTrackRepository.deleteAll();
        List<Integer> allTracks = appleClient.getAllTracksIds();
        List<List<Integer>> subSets = Lists.partition(allTracks, 300);
        AtomicInteger renduPage = new AtomicInteger(0);
        subSets.parallelStream().forEach( t ->{
            renduPage.getAndIncrement();
            log.info("{}/{}", renduPage.get(), subSets.size());
            List<AppleTrack> appleTrackByIds = appleClient.getAppleTrackByIds(t);
            appleTrackRepository.saveAll(appleTrackByIds);
        });
    }

    public List<AppleTrack> findBestOf() {
        return appleTrackRepository.findAppleTrackByRatingGreaterThanEqual(FOUR_STARS);
    }

    public List<AppleTrack> findNoneRated() {
        return appleTrackRepository.findAppleTrackByRatingEquals(NONE_RATING);
    }

    public List<AppleTrack> findTwoAndThreeStars() {
        List<AppleTrack> list = appleTrackRepository.findAppleTrackByRatingEquals(THREE_STARS);
        list.addAll(appleTrackRepository.findAppleTrackByRatingEquals(THREE_STARS));
        return list;
    }
}
