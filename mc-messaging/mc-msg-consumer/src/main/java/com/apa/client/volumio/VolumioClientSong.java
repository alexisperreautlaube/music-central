package com.apa.client.volumio;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VolumioClientSong {
    private String uri;
    private String service;
    private String title;
    private String artist;
    private String album;
    private String type = "song";
    private Integer trackNumber;
    private Integer duration;
    private String trackType;

}
