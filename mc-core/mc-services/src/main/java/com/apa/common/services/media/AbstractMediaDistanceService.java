package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.repositories.MediaDistanceRepository;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractMediaDistanceService<M extends MusicCentralMedia> implements MediaDistanceService<M> {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MongoConverter mongoConverter;

    @Autowired
    private MediaDistanceRepository mediaDistanceRepository;

    @Override
    @Transactional
    public MediaDistance save(MediaDistance mediaDistance) {
        if (MatchStatus.NO_PERFECT_MATCH.equals(mediaDistance.getMatchStatus())) {
            return saveNoPerfectMatch(mediaDistance);
        }
        Document documentToUpsert = new Document();
        mongoConverter.write(mediaDistance, documentToUpsert);
        UpdateResult updateResult = template.getCollection("mediaDistance")
                .replaceOne(
                        Filters.or(
                            Filters.and(
                                    Filters.eq("from._id", mediaDistance.getFrom().getId()),
                                    Filters.eq("from.clazz", mediaDistance.getFrom().getClazz()),
                                    Filters.eq("to._id", mediaDistance.getTo().getId()),
                                    Filters.eq("to.clazz", mediaDistance.getTo().getClazz())),
                            Filters.and(
                                    Filters.eq("from._id", mediaDistance.getTo().getId()),
                                    Filters.eq("from.clazz", mediaDistance.getTo().getClazz()),
                                    Filters.eq("to._id", mediaDistance.getFrom().getId()),
                                    Filters.eq("to.clazz", mediaDistance.getFrom().getClazz())),
                            Filters.and(
                                    Filters.eq("from._id", mediaDistance.getFrom().getId()),
                                    Filters.eq("from.clazz", mediaDistance.getFrom().getClazz()),
                                    Filters.eq("matchStatus", MatchStatus.NO_PERFECT_MATCH.toString())
                            ),
                            Filters.and(
                                    Filters.eq("from._id", mediaDistance.getTo().getId()),
                                    Filters.eq("from.clazz", mediaDistance.getTo().getClazz()),
                                    Filters.eq("matchStatus", MatchStatus.NO_PERFECT_MATCH.toString())
                            )
                        ),
                        documentToUpsert,
                        new ReplaceOptions().upsert(true));
        return mediaDistance;
    }

    private MediaDistance saveNoPerfectMatch(MediaDistance mediaDistance) {
        Document documentToUpsert = new Document();
        mongoConverter.write(mediaDistance, documentToUpsert);
        template.getCollection("mediaDistance")
                .replaceOne(
                        Filters.and(
                                Filters.eq("from._id", mediaDistance.getFrom().getId()),
                                Filters.eq("from.clazz", mediaDistance.getFrom().getClazz()),
                                Filters.eq("matchStatus", MatchStatus.NO_PERFECT_MATCH.toString())
                        ),
                        documentToUpsert,
                        new ReplaceOptions().upsert(true));
        return mediaDistance;
    }

    public abstract boolean hasPerfectMatchRecord(M p);

    protected boolean hasPerfectMatchRecord(String trackUri, Class<? extends M> aClass) {
        return !mediaDistanceRepository.findByFromIdAndFromClazz(trackUri, aClass.getName()).isEmpty();
    }
}
