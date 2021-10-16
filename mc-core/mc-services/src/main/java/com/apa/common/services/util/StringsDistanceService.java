package com.apa.common.services.util;

import com.apa.common.entities.util.StringsDistance;

public interface StringsDistanceService {

    StringsDistance save(StringsDistance stringsDistance);

    StringsDistance StringsDistance(String from, String to);
}
