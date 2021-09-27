package com.apa.common.repositories;

import com.apa.common.AbstractModelsIT;
import com.apa.common.entities.media.LocalMedia;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalMediaRepositoryIT extends AbstractModelsIT {

    @Autowired
    private LocalMediaRepository localMediaRepository;

    @Autowired
    private Javers javers;

    @Test
    public void reloadPreviousVersionTestTest() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID().toString())
                .localId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        LocalMedia localMedia = localMediaRepository.save(media);
        assertNotNull(localMedia);
        media.setTrackTitle("title2");
        localMediaRepository.save(media);
        Optional<LocalMedia> byId = localMediaRepository.findById(media.getUuid());
        assertEquals("title2", byId.get().getTrackTitle());
        QueryBuilder jqlQuery = QueryBuilder.byClass(LocalMedia.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        Assertions.assertEquals("title", snapshots.get(1).getPropertyValue("title"));
        Assertions.assertEquals("title2", snapshots.get(0).getPropertyValue("title"));
        assertNotNull(snapshots);
    }
}