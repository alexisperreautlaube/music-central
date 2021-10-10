package com.apa.common.services.util.impl;

import com.apa.common.entities.util.StringsDistance;
import com.apa.common.repositories.StringsDistanceRepository;
import com.apa.common.services.util.StringsDistanceService;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class StringsDistanceServiceImpl implements StringsDistanceService {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MongoConverter mongoConverter;

    @Autowired
    private StringsDistanceRepository stringsDistanceRepository;

    private LevenshteinDistance defaultInstance = LevenshteinDistance.getDefaultInstance();

    @Override
    @Transactional
    public StringsDistance StringsDistance(String from, String to) {
        return StringsDistance.builder()
                .from(from)
                .to(to)
                .distance(stringsDistanceRepository.findByFromAndTo(from, to)
                        .map(StringsDistance::getDistance)
                        .orElse(CalcutateDistance(from, to).getDistance()))
                .build();

    }

    private StringsDistance CalcutateDistance(String from, String to) {
        return save(StringsDistance.builder()
                .from(from)
                .to(to)
                .distance(defaultInstance.apply(from, to))
                .build());
    }

    private StringsDistance save(StringsDistance stringsDistance) {
        Document documentToUpsert = new Document();
        mongoConverter.write(stringsDistance, documentToUpsert);
        UpdateResult updateResult = template.getCollection("StringsDistance")
                .replaceOne(
                    Filters.and(Filters.eq("from", stringsDistance.getFrom()),
                            Filters.eq("from", stringsDistance.getFrom())),
                    documentToUpsert,
                    new ReplaceOptions().upsert(true));
        if (updateResult.getModifiedCount() == 0) {
            return stringsDistanceRepository.save(stringsDistance);
        }
        return stringsDistance;
    }
}
