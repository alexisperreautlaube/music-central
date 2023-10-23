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
public class AppleEqualizer {
    @Id
    private String uuid;

    private Integer index;

    private String name;

    public AppleEqualizer(Integer index, String name) {
        uuid = UUID.randomUUID().toString();
        this.index = index;
        this.name = name;
    }

}
