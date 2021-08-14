package com.apa.common.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class Media {
    @Id
    private UUID uuid;
    private String title;
    private String album;
    private String artist;
}
