package com.apa.common.services.util;

import com.apa.common.entities.util.ProducedMatch;
import com.apa.common.repositories.ProducedMatchRepository;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProduceMatchService {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private ProducedMatchRepository producedMatchRepository;

    public ProducedMatch save(ProducedMatch producedMatch) {
        return producedMatchRepository.save(producedMatch);
    }

    public boolean exist(ProducedMatch producedMatch) {
        FindIterable<Document> mediaDistance = template.getCollection("producedMatch")
                .find(
                        Filters.and(
                                Filters.eq("from._id", producedMatch.getFrom().getId()),
                                Filters.eq("from.clazz", producedMatch.getFrom().getClazz()),
                                Filters.eq("to._id", producedMatch.getTo().getId()),
                                Filters.eq("to.clazz", producedMatch.getTo().getClazz())
                        ));
        return mediaDistance.iterator().hasNext();
    }
}
