package com.apa.client.apple.service;

import com.apa.client.apple.entity.AppleAvailableTrack;
import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.repository.AppleAvailableTrackRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class AppleAvailableTrackService {

    public static final Short TWO_STARS = Short.valueOf("40");
    public static final Short THREE_STARS = Short.valueOf("60");
    public static final Short FOUR_STARS = Short.valueOf("80");
    public static final Short NONE_RATING = Short.valueOf("0");
    @Autowired
    private AppleTrackService appleTrackService;

    @Autowired
    private AppleAvailableTrackRepository appleAvailableTrackRepository;

    public List<Long> createTriageList() {
        Set<Long> bestOfTriageList = createBestOfTriageList();
        Set<Long> noneRatedTriageList = createNoneRatedTriageList(bestOfTriageList.size());
        Set<Long> reRatedTriageList = createReRatedTriageList(bestOfTriageList.size());
        List<Long> collect = new ArrayList<>();
        collect.addAll(bestOfTriageList);
        collect.addAll(noneRatedTriageList);
        collect.addAll(reRatedTriageList);
        Collections.shuffle(collect);
        return collect;
    }

    private Set<Long> createNoneRatedTriageList(int size) {
        List<AppleAvailableTrack> noneRated = appleAvailableTrackRepository.findAppleTrackByRatingEqualsOrderByReleaseDateDesc(NONE_RATING);
        int max = size / 2;

        return generateList(noneRated, max);
    }
    private Set<Long> createReRatedTriageList(int size) {
        List<AppleAvailableTrack> two = appleAvailableTrackRepository.findAppleTrackByRatingEqualsOrderByReleaseDateDesc(TWO_STARS);
        List<AppleAvailableTrack> three = appleAvailableTrackRepository.findAppleTrackByRatingEqualsOrderByReleaseDateDesc(THREE_STARS);
        int max = size / 4;
        List<AppleAvailableTrack> collect = Stream.concat(two.stream(), three.stream()).collect(Collectors.toList());

        return generateList(collect, max);
    }

    private Set<Long> createBestOfTriageList() {
        List<AppleAvailableTrack> bestOf = appleAvailableTrackRepository.findAppleTrackByRatingGreaterThanEqualOrderByReleaseDateDesc(FOUR_STARS);
        int max = bestOf.size() > 100 ? 100 : bestOf.size();
        return generateList(bestOf, max);
    }

    private static Set<Long> generateList(List<AppleAvailableTrack> list, int max) {
        List<Long> available = new ArrayList<>();
        list.forEach(appleAvailableTrack -> {
            for (int i = 0; i < appleAvailableTrack.getWeight(); i++) {
                available.add(appleAvailableTrack.getId());
            }
        });
        Set<Long> triage = new HashSet<>();
        Random rand = new Random();
        while ( triage.size() < max) {
            triage.add(available.get(rand.nextInt(available.size())));
        }
        return triage;
    }

    public void refreshWeight() {
        refreshBestOfWeight();
        refreshNoneRatedWeight();
        refreshTwoThreeStarsWeight();
    }

    public void refreshNoneRatedWeight() {
        List<AppleAvailableTrack> noneRated = appleAvailableTrackRepository.findAppleTrackByRatingEqualsOrderByReleaseDateDesc(NONE_RATING);
        AtomicInteger rendu = new AtomicInteger(1);
        noneRated.stream().parallel().forEach(appleAvailableTrack -> {
            int weight =
                    weightPlayedCount(appleAvailableTrack, noneRated) +
                            weightReleaseDate(rendu.getAndIncrement(), noneRated.size()) +
                            weightArtist(appleAvailableTrack) +
                            weightAlbum(appleAvailableTrack);
            appleAvailableTrack.setWeight(weight);
            appleAvailableTrackRepository.save(appleAvailableTrack);
            if (rendu.get() % 100 == 0) {
                log.info("{}/{}", rendu.get(), noneRated.size());
            }
        });
    }

    public void refreshTwoThreeStarsWeight() {
        List<AppleAvailableTrack> twoThreeStarsList = appleAvailableTrackRepository.findAppleTrackByRatingEqualsOrderByReleaseDateDesc(TWO_STARS);
        twoThreeStarsList.addAll(appleAvailableTrackRepository.findAppleTrackByRatingEqualsOrderByReleaseDateDesc(THREE_STARS));
        AtomicInteger rendu = new AtomicInteger(1);
        twoThreeStarsList.stream().parallel().forEach(appleAvailableTrack -> {
            int weight =
                    weightPlayedCount(appleAvailableTrack, twoThreeStarsList) +
                            weightReleaseDate(rendu.getAndIncrement(), twoThreeStarsList.size()) +
                            weightArtist(appleAvailableTrack) +
                            weightAlbum(appleAvailableTrack);
            appleAvailableTrack.setWeight(weight);
            appleAvailableTrackRepository.save(appleAvailableTrack);
            if (rendu.get() % 100 == 0) {
                log.info("{}/{}", rendu.get(), twoThreeStarsList.size());
            }
        });
    }

    public void refreshBestOfWeight() {
        List<AppleAvailableTrack> bestOf = appleAvailableTrackRepository.findAppleTrackByRatingGreaterThanEqualOrderByReleaseDateDesc(FOUR_STARS);
        AtomicInteger rendu = new AtomicInteger(1);
        bestOf.stream().parallel().forEach(appleAvailableTrack -> {
            int weight =
                    weightPlayedCount(appleAvailableTrack, bestOf) +
                    weightReleaseDate(rendu.getAndIncrement(), bestOf.size()) +
                    weightArtist(appleAvailableTrack) +
                    weightAlbum(appleAvailableTrack) +
                            (appleAvailableTrack.getRating() / 20);
            appleAvailableTrack.setWeight(weight);
            appleAvailableTrackRepository.save(appleAvailableTrack);
            if (rendu.get() % 100 == 0) {
                log.info("{}/{}", rendu.get(), bestOf.size());
            }
        });
    }


    private int weightAlbum(AppleAvailableTrack appleAvailableTrack) {
        if (StringUtils.isBlank(appleAvailableTrack.getAlbum())) {
            return 0;
        }
        return appleAvailableTrackRepository.countAppleAvailableTrackByArtistEqualsIgnoreCaseAndAndAlbumEqualsIgnoreCaseAndRatingGreaterThanEqual(appleAvailableTrack.getArtist(), appleAvailableTrack.getAlbum(), FOUR_STARS);
    }

    private int weightArtist(AppleAvailableTrack appleAvailableTrack) {
        return appleAvailableTrackRepository.countAppleAvailableTrackByArtistEqualsIgnoreCaseAndRatingGreaterThanEqual(appleAvailableTrack.getArtist(), FOUR_STARS);
    }

    private int weightReleaseDate(int rank, int listSize) {
        return getWeightScore(rank, listSize);
    }

    private int weightPlayedCount(AppleAvailableTrack appleAvailableTrack,List<AppleAvailableTrack> tracks) {
        AtomicInteger maxPlayedCount = new AtomicInteger(0);
        tracks.forEach( listTrack -> {
            if (listTrack.getPlayedCount() > maxPlayedCount.get()) {
                maxPlayedCount.set(listTrack.getPlayedCount());
            }
        });
        return getWeightScore(maxPlayedCount.get(), appleAvailableTrack.getPlayedCount());
    }

    private int getWeightScore(int actual, Integer max) {
        Long firstPercentile = Long.valueOf("" + max) / 10;
        if (actual >= firstPercentile * 10) {
            return 1;
        } else if (actual >= firstPercentile * 9) {
            return 2;
        } else if (actual >= firstPercentile * 8) {
            return 3;
        } else if (actual >= firstPercentile * 7) {
            return 4;
        } else if (actual >= firstPercentile * 6) {
            return 5;
        } else if (actual >= firstPercentile * 5) {
            return 6;
        } else if (actual >= firstPercentile * 4) {
            return 7;
        } else if (actual >= firstPercentile * 3) {
            return 8;
        } else if (actual >= firstPercentile * 2) {
            return 9;
        } else {
            return 10;
        }
    }


    public void refreshAppleAvailableTrack() {
        appleAvailableTrackRepository.deleteAll();
        refreshBestOf();
        refreshNoneRated();
        refreshTwoThreeStars();
    }

    private void refreshNoneRated() {
        List<AppleAvailableTrack> noneRated = appleTrackService.findNoneRated().stream().map(appleTrack ->
                fromAppleTrack(appleTrack)
        ).collect(Collectors.toList());
        appleAvailableTrackRepository.saveAll(noneRated);
    }
    private void refreshTwoThreeStars() {
        List<AppleAvailableTrack> noneRated = appleTrackService.findTwoAndThreeStars().stream().map(appleTrack ->
                fromAppleTrack(appleTrack)
        ).collect(Collectors.toList());
        appleAvailableTrackRepository.saveAll(noneRated);
    }

    private static AppleAvailableTrack fromAppleTrack(AppleTrack appleTrack) {
        return AppleAvailableTrack.builder()
                .id(appleTrack.getId())
                .releaseDate(appleTrack.getReleaseDate())
                .rating(appleTrack.getRating())
                .playedCount(appleTrack.getPlayedCount())
                .artist(appleTrack.getArtist())
                .album(appleTrack.getAlbum())
                .track(appleTrack.getName())
                .build();
    }

    private void refreshBestOf() {
        List<AppleAvailableTrack> bestOf = appleTrackService.findBestOf().stream().map(appleTrack ->
                fromAppleTrack(appleTrack)
        ).collect(Collectors.toList());
        appleAvailableTrackRepository.saveAll(bestOf);
    }

    public List<Long> createBestOf() {
        Set<Long> bestOfTriageList = createBestOfTriageList();
        List<Long> collect = bestOfTriageList.stream().collect(Collectors.toList());
        Collections.shuffle(collect);
        return collect;
    }
}
