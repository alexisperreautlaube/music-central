package com.apa.client.apple.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Equalizer {
    long preamp;
    long f32;
    long f64;
    long f125;
    long f250;
    long f500;
    long f1000;
    long f2000;
    long f4000;
    long f8000;
    long f16000;
}
