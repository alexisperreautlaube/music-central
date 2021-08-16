package com.apa.importer.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TidalMediaDto extends MediaDto {
    private String tidalId;
}
