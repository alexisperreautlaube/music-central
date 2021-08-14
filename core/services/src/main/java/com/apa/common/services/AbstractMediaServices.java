package com.apa.common.services;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.Media;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class AbstractMediaServices<E extends Media> implements MediaService<E> {
    @Autowired
    Javers javers;

    protected long getEntityVersion(E entity) {
        Optional<CdoSnapshot> latestSnapshot = javers.getLatestSnapshot(entity.getUuid(), entity.getClass());
        return latestSnapshot.get().getVersion();
    }

    public VersionMedia<E> restore(E media, int i) {
        JqlQuery jqlQuery = QueryBuilder.byClass(media.getClass()).withVersion(i).build();
        List<Shadow<E>> shadows = javers.findShadows(jqlQuery);
        return save(shadows.get(0).get());
    }

    @Override
    public abstract VersionMedia<E> save(E media);
}
