package com.apa.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorMessage {
    private String topic;
    private Object payload;
    private Exception exception;
}
