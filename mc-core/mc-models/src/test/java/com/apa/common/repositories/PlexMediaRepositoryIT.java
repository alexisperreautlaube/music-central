package com.apa.common.repositories;

import com.apa.common.AbstractModelsIT;
import com.apa.common.entities.media.PlexMedia;
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

class PlexMediaRepositoryIT extends AbstractModelsIT {

    @Autowired
    private PlexMediaRepository plexMediaRepository;

    @Autowired
    private Javers javers;

    @Test
    public void reloadPreviousVersionTestTest() {
        PlexMedia media = PlexMedia.builder()
                .uuid(UUID.randomUUID().toString())
                .plexId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        PlexMedia plexMedia = plexMediaRepository.save(media);
        assertNotNull(plexMedia);
        media.setTrackTitle("title2");
        plexMediaRepository.save(media);
        Optional<PlexMedia> byId = plexMediaRepository.findById(media.getUuid());
        assertEquals("title2", byId.get().getTrackTitle());
        QueryBuilder jqlQuery = QueryBuilder.byClass(PlexMedia.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        Assertions.assertEquals("title", snapshots.get(1).getPropertyValue("title"));
        Assertions.assertEquals("title2", snapshots.get(0).getPropertyValue("title"));
        assertNotNull(snapshots);
    }
}