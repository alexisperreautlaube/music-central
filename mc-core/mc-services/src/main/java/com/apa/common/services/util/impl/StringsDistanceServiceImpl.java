package com.apa.common.services.util.impl;

import com.apa.common.entities.util.StringsDistance;
import com.apa.common.services.util.StringsDistanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class StringsDistanceServiceImpl implements StringsDistanceService {

    private LevenshteinDistance defaultInstance = LevenshteinDistance.getDefaultInstance();

    @Override
    @Transactional
    public StringsDistance StringsDistance(String from, String to) {
        return StringsDistance.builder()
                .from(from)
                .to(to)
                .distance(calcutateDistance(from, to).getDistance())
                .build();
    }

    private StringsDistance calcutateDistance(String from, String to) {
        return StringsDistance.builder()
                .from(from)
                .to(to)
                .distance(defaultInstance.apply(from, to))
                .build();
    }
}
