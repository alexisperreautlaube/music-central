package com.apa.importer.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MediaDto {
    private String title;
    private String album;
    private String artist;
}
