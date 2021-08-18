package com.apa.core.dto.media;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class LocalMediaDto extends MediaDto {
    private String localId;
}
