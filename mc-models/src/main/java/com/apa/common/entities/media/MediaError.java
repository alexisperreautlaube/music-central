package com.apa.common.entities.media;

import com.apa.common.entities.enums.MediaErrorStatus;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MediaError {
    private MediaErrorStatus mediaErrorStatus;
    private String uri;
    private String artist;
    private String album;
    private String track;
}
