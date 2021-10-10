package com.apa.common.repositories;

import com.apa.common.AbstractModelsIT;
import com.apa.common.entities.util.StringsDistance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringsDistanceRepositoryIT extends AbstractModelsIT {

    @Autowired
    private StringsDistanceRepository stringsDistanceRepository;

    @Test
    public void testIndex() {
        StringsDistance stringsDistance1 = StringsDistance.builder()
                .from("from")
                .to("to")
                .build();

        StringsDistance stringsDistance2 = StringsDistance.builder()
                .from("from")
                .to("to")
                .build();
        stringsDistanceRepository.save(stringsDistance1);
        stringsDistanceRepository.save(stringsDistance2);
        int size = stringsDistanceRepository.findAll().size();
        assertEquals(1, size);
    }
}