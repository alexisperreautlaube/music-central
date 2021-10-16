package com.apa.common.msg.producer;

import com.apa.core.dto.media.TidalMediaDto;
import com.apa.core.dto.media.VolumioMediaDto;

public class TidalVolumioMediaData extends MediaMediaData<TidalMediaDto, VolumioMediaDto> {

    TidalVolumioMediaData(TidalMediaDto from, VolumioMediaDto to) {
        super(from, to);
    }
}
