package com.apa.common.services.util.impl;

import com.apa.common.AbstractCommonIT;
import com.apa.common.services.util.StringsDistanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringsDistanceServiceImplIT extends AbstractCommonIT {

    @Autowired
    private StringsDistanceService stringsDistanceService;

    @Test
    public void save() {
        assertEquals(3, stringsDistanceService.StringsDistance("from", "to"));
    }
}