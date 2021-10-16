package com.apa.common.msg.producer;

import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.VolumioMediaDto;

public class VolumioPlexMediaData extends MediaMediaData<VolumioMediaDto, PlexMediaDto> {
    VolumioPlexMediaData(VolumioMediaDto from, PlexMediaDto to) {
        super(from, to);
    }
}
