package com.apa.common.repositories;

import com.apa.common.AbstractModelsIT;
import com.apa.common.entities.media.TidalMedia;
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

class TidalMediaRepositoryIT extends AbstractModelsIT {

    @Autowired
    private TidalMediaRepository tidalMediaRepository;

    @Autowired
    private Javers javers;

    @Test
    public void reloadPreviousVersionTestTest() {
        TidalMedia media = TidalMedia.builder()
                .uuid(UUID.randomUUID())
                .tidalTrackId(UUID.randomUUID().toString())
                .trackTitle("title")
                .albumName("album")
                .artistName("artist")
                .build();
        TidalMedia tidalMedia = tidalMediaRepository.save(media);
        assertNotNull(tidalMedia);
        media.setTrackTitle("title2");
        tidalMediaRepository.save(media);
        Optional<TidalMedia> byId = tidalMediaRepository.findById(media.getUuid());
        assertEquals("title2", byId.get().getTrackTitle());
        QueryBuilder jqlQuery = QueryBuilder.byClass(TidalMedia.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        Assertions.assertEquals("title", snapshots.get(1).getPropertyValue("title"));
        Assertions.assertEquals("title2", snapshots.get(0).getPropertyValue("title"));
        assertNotNull(snapshots);
    }
}