package com.apa.common.repositories;

import com.apa.common.entities.LocalMedia;
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

class LocalRepositoryIT extends AbstractCommonIT {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private Javers javers;

    @Test
    public void reloadPreviousVersionTestTest() {
        LocalMedia media = LocalMedia.builder()
                .uuid(UUID.randomUUID())
                .localId(UUID.randomUUID().toString())
                .title("title")
                .album("album")
                .artist("artist")
                .build();
        LocalMedia localMedia = localRepository.save(media);
        assertNotNull(localMedia);
        media.setTitle("title2");
        localRepository.save(media);
        Optional<LocalMedia> byId = localRepository.findById(media.getUuid());
        assertEquals("title2", byId.get().getTitle());
        QueryBuilder jqlQuery = QueryBuilder.byClass(LocalMedia.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        assertEquals("title", snapshots.get(1).getPropertyValue("title"));
        assertEquals("title2", snapshots.get(0).getPropertyValue("title"));
        assertNotNull(snapshots);
    }
}