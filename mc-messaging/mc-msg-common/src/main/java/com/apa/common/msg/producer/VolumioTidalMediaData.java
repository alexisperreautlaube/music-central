package com.apa.common.msg.producer;

import com.apa.core.dto.media.TidalMediaDto;
import com.apa.core.dto.media.VolumioMediaDto;

public class VolumioTidalMediaData extends MediaMediaData<VolumioMediaDto, TidalMediaDto> {

    VolumioTidalMediaData(VolumioMediaDto from, TidalMediaDto to) {
        super(from, to);
    }
}
