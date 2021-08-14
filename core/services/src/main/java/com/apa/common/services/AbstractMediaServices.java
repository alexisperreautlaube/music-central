package com.apa.common.services;

import com.apa.common.entities.media.Media;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AbstractMediaServices<E extends Media> {
    @Autowired
    Javers javers;

    protected long getEntityVersion(E entity) {
        Optional<CdoSnapshot> latestSnapshot = javers.getLatestSnapshot(entity.getUuid(), entity.getClass());
        return latestSnapshot.get().getVersion();
    }
}
