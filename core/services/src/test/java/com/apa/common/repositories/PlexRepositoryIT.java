package com.apa.common.repositories;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.AbstractCommonIT;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlexRepositoryIT extends AbstractCommonIT {

    @Autowired
    private PlexRepository plexRepository;

    @Autowired
    private Javers javers;

    @Test
    public void reloadPreviousVersionTestTest() {
        PlexMedia media = PlexMedia.builder()
                .uuid(UUID.randomUUID())
                .plexId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        PlexMedia plexMedia = plexRepository.save(media);
        assertNotNull(plexMedia);
        media.setTitle("title2");
        plexRepository.save(media);
        Optional<PlexMedia> byId = plexRepository.findById(media.getUuid());
        assertEquals("title2", byId.get().getTitle());
        QueryBuilder jqlQuery = QueryBuilder.byClass(PlexMedia.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        assertEquals("title", snapshots.get(1).getPropertyValue("title"));
        assertEquals("title2", snapshots.get(0).getPropertyValue("title"));
        assertNotNull(snapshots);
    }
}