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
@EqualsAndHashCode
@NoArgsConstructor
public class MusicCentralMedia {
    @Id
    private UUID uuid;
    private String title;
    private String album;
    private String artist;
}
