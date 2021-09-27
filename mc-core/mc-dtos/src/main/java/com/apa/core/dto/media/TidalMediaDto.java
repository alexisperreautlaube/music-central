package com.apa.core.dto.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
public class TidalMediaDto extends MediaDto {
    private String artistId;
    private Map<String, String> artists;
    private String albumId;
    private int trackCount;
    private int discCount;
    private long albumDuration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String tidalTrackId;
    private long trackDuration;
    private int trackNumber;
    private int discNumber;
    private String trackVersion;
    private int popularity;
    private boolean available;
    private String type;
}
