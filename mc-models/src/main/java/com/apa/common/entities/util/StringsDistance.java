package com.apa.common.entities.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
@CompoundIndex(def = "{'from': 1, 'to': 1}", unique=true)
public class StringsDistance {
    @Id
    private String id;
    private String from;
    private String to;
    private Integer distance;
}
