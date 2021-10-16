package com.apa.common.msg.producer;

import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.TidalMediaDto;

public class TidalPlexMediaData extends MediaMediaData<TidalMediaDto, PlexMediaDto> {
    TidalPlexMediaData(TidalMediaDto from, PlexMediaDto to) {
        super(from, to);
    }
}
