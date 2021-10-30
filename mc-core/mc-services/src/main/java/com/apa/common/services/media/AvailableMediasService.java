package com.apa.common.services.media;

import com.apa.common.entities.enums.MediaQuality;
import com.apa.common.entities.media.*;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.repositories.AvailableMediasRepository;
import com.apa.common.services.media.impl.RatingService;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaDistanceService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class AvailableMediasService {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private AvailableMediasRepository availableMediasRepository;

    @Autowired
    private VolumioMediaDistanceService volumioMediaDistanceService;

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private RatingService ratingService;

    public List<String> createTriageIdList() {
        Set<String> ids = new HashSet<>();
        List<AvailableMedias> bestOf = availableMediasRepository.findByRatingGreaterThan(3);
        Random rand = new Random();
        while (ids.size() != 300) {
            ids.add(bestOf.get(rand.nextInt(bestOf.size())).getId());
        }
        List<AvailableMedias> noneRated = availableMediasRepository.findByRating(0);
        while (ids.size() != 500) {
            ids.add(noneRated.get(rand.nextInt(noneRated.size())).getId());
        }
        List<String> list = new ArrayList<>(ids);
        Collections.shuffle(list);
        return list;
    }

    public void createAvailableList() {
        volumioMediaService.findAll().forEach(
            v -> createSingleAvailable(v)
        );
    }

    @Transactional
    public void createSingleAvailable(VolumioMedia volumioMedia) {
        if (entryExist(volumioMedia)) {
            return;
        }
        List<RelatedMedia> relatedMediaList = new ArrayList<>();
        relatedMediaList.add(RelatedMedia.builder()
                        .id(volumioMedia.getTrackUri())
                        .clazz(volumioMedia.getClass().getName())
                        .quality(MediaQuality.fromString(volumioMedia.getAlbumAudioQuality()))
                .build());
        List<MediaDistance> matches = volumioMediaDistanceService.getMatches(volumioMedia);
        matches.forEach(m -> {
            Optional<RelatedMedia> relatedMedia = toAvailable(volumioMedia, m);
            if (relatedMedia.isPresent()) {
                relatedMediaList.add(relatedMedia.get());
            }
        });
        availableMediasRepository.save(initAvailableMedias(relatedMediaList));
    }

    private AvailableMedias initAvailableMedias(List<RelatedMedia> relatedMediaList) {
        AvailableMedias availableMedias = initIdAndRelatedList(relatedMediaList);
        initPlayCount(availableMedias);
        initImportRating(availableMedias);
        initManualRating(availableMedias);
        return availableMedias;
    }

    private void initManualRating(AvailableMedias availableMedias) {
        availableMedias.getRelatedMedias().stream()
                .filter(r -> r.getClazz().equals(VolumioMedia.class.getName()))
                .findFirst().ifPresent( r -> {
                            Optional<Rating> latestRating = ratingService.getLatestRating(r.getId(), r.getClazz());
                            latestRating.ifPresent( l ->
                                    availableMedias.setRating(l.getRating())
                            );
                        }
                );
    }

    private void initPlayCount(AvailableMedias availableMedias) {
        int playCount = availableMedias.getRelatedMedias().stream().filter(relatedMedia -> relatedMedia.getPlayCount() != null).mapToInt(RelatedMedia::getPlayCount).sum();
        availableMedias.setPlayCount(playCount);
    }

    private void initImportRating(AvailableMedias availableMedias) {
        Optional<Integer> max = availableMedias.getRelatedMedias().stream().filter(relatedMedia -> relatedMedia.getRating() != null).map(RelatedMedia::getRating).map(RelatedMedia.RelatedMediaRating::getRating).max(Integer::compare);
        availableMedias.setRating(max.orElse(0));
    }

    private AvailableMedias initIdAndRelatedList(List<RelatedMedia> relatedMediaList) {
        Optional<RelatedMedia> any = relatedMediaList.stream()
                .filter(relatedMedia -> relatedMedia.getClazz().equals(VolumioMedia.class.getName()) && relatedMedia.getQuality().equals(MediaQuality.TIDAL)).findAny();
        if (any.isPresent()) {
            return AvailableMedias.builder()
                    .id(any.get().getId())
                    .relatedMedias(relatedMediaList)
                    .build();
        }
        any = relatedMediaList.stream()
                .filter(relatedMedia -> relatedMedia.getClazz().equals(VolumioMedia.class.getName()) && relatedMedia.getQuality().equals(MediaQuality.FLAC)).findAny();
        if (any.isPresent()) {
            return AvailableMedias.builder()
                    .id(any.get().getId())
                    .relatedMedias(relatedMediaList)
                    .build();
        }
        any = relatedMediaList.stream()
                .filter(relatedMedia -> relatedMedia.getClazz().equals(VolumioMedia.class.getName()) && relatedMedia.getQuality().equals(MediaQuality.AIFF)).findAny();
        if (any.isPresent()) {
            return AvailableMedias.builder()
                    .id(any.get().getId())
                    .relatedMedias(relatedMediaList)
                    .build();
        }
        any = relatedMediaList.stream()
                .filter(relatedMedia -> relatedMedia.getClazz().equals(VolumioMedia.class.getName())).findAny();
        if (any.isPresent()) {
            return AvailableMedias.builder()
                    .id(any.get().getId())
                    .relatedMedias(relatedMediaList)
                    .build();
        }
        throw new IllegalArgumentException("no volumio media");
    }

    private Optional<RelatedMedia> toAvailable(VolumioMedia volumioMedia, MediaDistance mediaDistance) {
        Optional<MediaReference> mediaReference = getOther(volumioMedia, mediaDistance);
        if (!mediaReference.isPresent()) {
            return Optional.empty();
        }
        if (mediaReference.get().getClazz().equals(PlexMedia.class.getName())) {
            return Optional.of(getPlex(mediaReference.get()));
        } else if (mediaReference.get().getClazz().equals(TidalMedia.class.getName())) {
            return Optional.of(getTidal(mediaReference.get()));
        } else if (mediaReference.get().getClazz().equals(VolumioMedia.class.getName())) {
            return Optional.of(getVolumio(mediaReference.get()));
        }
        throw new IllegalArgumentException("invalid clazz");
    }

    private RelatedMedia getPlex(MediaReference mediaReference) {
        PlexMedia byId = plexMediaService.findById(mediaReference.getId());
        return RelatedMedia.builder()
                .id(byId.getPlexId())
                .clazz(byId.getClass().getName())
                .playCount(byId.getTrackViewCount())
                .quality(MediaQuality.fromString(byId.getTrackFormat()))
                .rating(RelatedMedia.RelatedMediaRating.builder()
                        .rating(byId.getTrackUserRating().intValue() / 2)
                        .rateDate(byId.getAddedAt())
                        .build())
                .addedDate(byId.getAddedAt())
                .build();
    }

    private RelatedMedia getTidal(MediaReference mediaReference) {
        TidalMedia byId = tidalMediaService.findById(mediaReference.getId());
        return RelatedMedia.builder()
                .id(byId.getTidalTrackId())
                .clazz(byId.getClass().getName())
                .quality(MediaQuality.TIDAL)
                .build();
    }

    private RelatedMedia getVolumio(MediaReference mediaReference) {
        VolumioMedia byId = volumioMediaService.findById(mediaReference.getId());
        return RelatedMedia.builder()
                .id(byId.getTrackUri())
                .clazz(byId.getClass().getName())
                .quality(MediaQuality.fromString(byId.getAlbumAudioQuality()))
                .build();
    }


    private Optional<MediaReference> getOther(VolumioMedia volumioMedia, MediaDistance mediaDistance) {
        if (mediaDistance.getFrom().getId().equals(volumioMedia.getTrackUri()) && mediaDistance.getFrom().getClazz().equals(volumioMedia.getClass().getName())) {
            return Optional.ofNullable(mediaDistance.getTo());
        } else if (mediaDistance.getTo().getId().equals(volumioMedia.getTrackUri()) && mediaDistance.getTo().getClazz().equals(volumioMedia.getClass().getName())) {
            return Optional.of(mediaDistance.getFrom());
        }
        throw new IllegalArgumentException("no hit");
    }

    public Integer getRating(String uri) {
        return availableMediasRepository.findById(uri).map(AvailableMedias::getRating).orElse(0);
    }

    private boolean entryExist(VolumioMedia volumioMedia) {
        FindIterable<Document> result = template.getCollection("mediaDistance")
                .find(Filters.and(
                        Filters.eq("relatedMedias.id", volumioMedia.getTrackUri()),
                        Filters.eq("relatedMedias.clazz", volumioMedia.getClass().getName()))
                );
        return result.iterator().hasNext();
    }
}
