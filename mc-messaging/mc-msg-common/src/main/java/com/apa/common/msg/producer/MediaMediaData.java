package com.apa.common.msg.producer;

import com.apa.core.dto.media.MediaDto;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class MediaMediaData<F extends MediaDto, T extends MediaDto> {
    private F from;
    private T to;
}
