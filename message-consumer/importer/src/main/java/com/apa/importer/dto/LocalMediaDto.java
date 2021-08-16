package com.apa.importer.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LocalMediaDto extends MediaDto {
    private String localId;
}
