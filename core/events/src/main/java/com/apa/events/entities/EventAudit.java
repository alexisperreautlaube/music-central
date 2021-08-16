package com.apa.events.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class EventAudit {
    private UUID uuid;
    private Class clazz;
    private long version;
}
