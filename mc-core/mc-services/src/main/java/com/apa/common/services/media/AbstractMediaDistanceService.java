package com.apa.common.services.media;

import com.apa.common.entities.media.MusicCentralMedia;
import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.StringsDistance;
import com.apa.common.repositories.MediaDistanceRepository;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
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
        return !mediaDistanceRepository.findByFromIdAndFromClazzAndMatchStatus(trackUri, aClass.getName(), MatchStatus.PERFECT_MATCH.name()).isEmpty();
    }

    protected boolean isPerfectMatch(StringsDistance artist, StringsDistance album, StringsDistance song) {
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        return distanceTotal == 0;
    }

    protected boolean isGoodForAutomaticMatch(StringsDistance artist, StringsDistance album, StringsDistance song) {
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        return distanceTotal <= 15
                && distanceTotal != 0
                && artist.getDistance() <= 5
                && album.getDistance() <= 5
                && song.getDistance() <= 5;
    }

    protected boolean isGoodForManualEvaluationFar(StringsDistance artist, StringsDistance album, StringsDistance song) {
        int distanceTotal = artist.getDistance() + album.getDistance() + song.getDistance();
        return distanceTotal <= 25
                && distanceTotal != 0
                && artist.getDistance() <= 10
                && album.getDistance() <= 10
                && song.getDistance() <= 10;
    }

    protected boolean isInvalidForDistance(PlexMedia plexMedia) {
        return StringUtils.isBlank(plexMedia.getArtistName())
                || StringUtils.isBlank(plexMedia.getAlbumName())
                || StringUtils.isBlank(plexMedia.getTrackTitle());
    }

    protected boolean isInvalidForDistance(VolumioMedia volumioMedia) {
        return StringUtils.isBlank(volumioMedia.getTrackArtist())
                || StringUtils.isBlank(volumioMedia.getAlbumTitle())
                || StringUtils.isBlank(volumioMedia.getTrackTitle());
    }

    protected boolean isInvalidForDistance(TidalMedia tidalMedia) {
        return StringUtils.isBlank(tidalMedia.getArtistName())
                || StringUtils.isBlank(tidalMedia.getAlbumName())
                || StringUtils.isBlank(tidalMedia.getTrackTitle());
    }
}
