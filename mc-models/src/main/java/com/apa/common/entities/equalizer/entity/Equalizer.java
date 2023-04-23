package com.apa.common.entities.equalizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Equalizer {
    private String gender;
    private String artist;
    private String album;
    private long preamp;
    private long f32;
    private long f64;
    private long f125;
    private long f250;
    private long f500;
    private long f1000;
    private long f2000;
    private long f4000;
    private long f8000;
    private long f16000;
}
