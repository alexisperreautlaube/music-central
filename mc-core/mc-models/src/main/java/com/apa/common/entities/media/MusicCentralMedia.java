package com.apa.common.entities.media;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(exclude="uuid")
@NoArgsConstructor
public class MusicCentralMedia {
    @Id
    private String uuid;
    private String trackTitle;
    private String albumName;
    private String artistName;
}
