package com.apa.common.services;

import com.apa.common.entities.VersionMedia;
import com.apa.common.entities.media.MusicCentralMedia;
import lombok.Getter;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractMediaService<E extends MusicCentralMedia> implements MediaService<E> {
    @Autowired
    private Javers javers;

    @Getter
    private final Class<E> persistentClass;

    public AbstractMediaService() {
        this.persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected long getEntityVersion(E entity) {
        Optional<CdoSnapshot> latestSnapshot = javers.getLatestSnapshot(entity.getUuid(), entity.getClass());
        return latestSnapshot.get().getVersion();
    }

    public VersionMedia<E> restore(String uuid, long i) {
        if (i == 0) {
            delete(uuid);
            return new VersionMedia(0, null);
        }
        JqlQuery jqlQuery = QueryBuilder.byClass(persistentClass).withVersion(i).build();
        List<Shadow<E>> shadows = javers.findShadows(jqlQuery);
        return save(shadows.get(0).get());
    }

    @Override
    public abstract VersionMedia<E> save(E media);

    @Override
    public abstract void delete(String uuid);

    public abstract boolean existAndEquals(E media);
}
