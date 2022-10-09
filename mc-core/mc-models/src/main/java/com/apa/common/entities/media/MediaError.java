package com.apa.common.entities.media;

import com.apa.common.entities.enums.MediaErrorStatus;
import com.apa.common.entities.util.MediaReference;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MediaError {
    MediaReference mediaReference;
    MediaErrorStatus mediaErrorStatus;
    String artist;
    String album;
}
