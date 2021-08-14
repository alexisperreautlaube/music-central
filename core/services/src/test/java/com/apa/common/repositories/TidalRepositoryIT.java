package com.apa.common.repositories;

import com.apa.common.entities.media.TidalMedia;
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

class TidalRepositoryIT extends AbstractCommonIT {

    @Autowired
    private TidalRepository tidalRepository;

    @Autowired
    private Javers javers;

    @Test
    public void reloadPreviousVersionTestTest() {
        TidalMedia media = TidalMedia.builder()
                .uuid(UUID.randomUUID())
                .tidalId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        TidalMedia tidalMedia = tidalRepository.save(media);
        assertNotNull(tidalMedia);
        media.setTitle("title2");
        tidalRepository.save(media);
        Optional<TidalMedia> byId = tidalRepository.findById(media.getUuid());
        assertEquals("title2", byId.get().getTitle());
        QueryBuilder jqlQuery = QueryBuilder.byClass(TidalMedia.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        assertEquals("title", snapshots.get(1).getPropertyValue("title"));
        assertEquals("title2", snapshots.get(0).getPropertyValue("title"));
        assertNotNull(snapshots);
    }
}