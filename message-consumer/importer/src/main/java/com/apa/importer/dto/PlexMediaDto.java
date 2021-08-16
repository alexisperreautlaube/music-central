package com.apa.importer.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PlexMediaDto extends MediaDto {
    private String plexId;
}
