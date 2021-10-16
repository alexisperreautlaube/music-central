package com.apa.common.msg.producer;

import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.VolumioMediaDto;

public class PlexVolumioMediaData extends MediaMediaData<PlexMediaDto, VolumioMediaDto> {
    PlexVolumioMediaData(PlexMediaDto from, VolumioMediaDto to) {
        super(from, to);
    }
}
