package com.apa.common.entities.enums;

public enum MediaQuality {

    MP3,
    AIFF,
    FLAC,
    TIDAL;

    public static MediaQuality fromString(String value) {
        for(MediaQuality v : values()) {
            if (v.name().equalsIgnoreCase(value)) {
                return v;
            }
        }
        return MP3;
    }
}
