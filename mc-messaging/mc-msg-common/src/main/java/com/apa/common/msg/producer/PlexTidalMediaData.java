package com.apa.common.msg.producer;

import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.TidalMediaDto;

public class PlexTidalMediaData extends MediaMediaData<PlexMediaDto, TidalMediaDto> {
    PlexTidalMediaData(PlexMediaDto from, TidalMediaDto to) {
        super(from, to);
    }
}
