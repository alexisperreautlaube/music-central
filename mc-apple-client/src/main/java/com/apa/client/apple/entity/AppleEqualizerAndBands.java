package com.apa.client.apple.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AppleEqualizerAndBands {

    private String name;

    private double band1;
    private double band2;
    private double band3;
    private double band4;
    private double band5;
    private double band6;
    private double band7;
    private double band8;
    private double band9;
    private double band10;
    private double preamp;

}
