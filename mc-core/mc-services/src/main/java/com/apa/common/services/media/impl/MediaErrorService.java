package com.apa.common.services.media.impl;

import com.apa.common.entities.enums.MediaErrorStatus;
import com.apa.common.entities.media.MediaError;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.repositories.MediaErrorRepository;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MediaErrorService {

    @Autowired
    private MediaErrorRepository mediaErrorRepository;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MongoConverter mongoConverter;

    public boolean doesNotExistOrExistWithErrorStatus(String id, String clazz) {
        return doesNotExistOrExistWithStatus(id, clazz, MediaErrorStatus.ERROR);
    }
    public boolean doesNotExistOrExistWithStatus(String id, String clazz, MediaErrorStatus... mediaErrorStatuses) {
        Optional<MediaError> byMediaReferenceIdAndMediaReferenceClazz = mediaErrorRepository.findByMediaReferenceIdAndMediaReferenceClazz(id, clazz);
        return byMediaReferenceIdAndMediaReferenceClazz.isEmpty()
                || ArrayUtils.contains(mediaErrorStatuses, byMediaReferenceIdAndMediaReferenceClazz.get().getMediaErrorStatus());
    }

    public boolean doesNotExistOrExistWithStatusElse(String id, String clazz, MediaErrorStatus... mediaErrorStatuses) {
        Optional<MediaError> byMediaReferenceIdAndMediaReferenceClazz = mediaErrorRepository.findByMediaReferenceIdAndMediaReferenceClazz(id, clazz);
        return byMediaReferenceIdAndMediaReferenceClazz.isEmpty()
                || !ArrayUtils.contains(mediaErrorStatuses, byMediaReferenceIdAndMediaReferenceClazz.get().getMediaErrorStatus());
    }

    public void saveDetemineStatus(String id, String clazz, String artist, String album) {
        MediaErrorStatus mediaErrorStatus = determineStatus(artist, album, clazz);
        if (doesNotExistOrExistWithStatus(id, clazz, mediaErrorStatus)) {
            save(id, clazz, artist, album, mediaErrorStatus);
        }
    }

    public void save(String id, String clazz, String artist, String album, MediaErrorStatus mediaErrorStatus) {
        MediaError mediaError = MediaError.builder()
                .artist(artist)
                .album(album)
                .mediaReference(MediaReference.builder()
                        .id(id)
                        .clazz(clazz)
                        .build())
                .mediaErrorStatus(mediaErrorStatus)
                .build();
        Document documentToUpsert = new Document();
        mongoConverter.write(mediaError, documentToUpsert);
        template.getCollection("mediaError")
                .replaceOne(
                        Filters.and(
                                Filters.eq("mediaReference._id", id),
                                Filters.eq("mediaReference.clazz", clazz)
                        ),
                        documentToUpsert,
                        new ReplaceOptions().upsert(true));
    }

    private MediaErrorStatus determineStatus(String artist, String album, String clazz) {
        if (!VolumioMedia.class.getName().equals(clazz)) {
            return MediaErrorStatus.ERROR;
        }
        if (volumioMediaService.exist(artist, album)) {
            return MediaErrorStatus.DUPLICATED;
        }
        return MediaErrorStatus.ERROR;
    }
}
