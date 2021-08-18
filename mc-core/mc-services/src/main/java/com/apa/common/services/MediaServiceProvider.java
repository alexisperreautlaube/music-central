package com.apa.common.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MediaServiceProvider {

    @Autowired
    private List<AbstractMediaService> abstractMediaServices;

    public Optional<AbstractMediaService> provideMediaService(Class clazz) {
        AtomicReference<AbstractMediaService> localAbstractMediaService = new AtomicReference<>();
        abstractMediaServices.forEach(
                abstractMediaService ->  {
                    if (abstractMediaService.getPersistentClass().equals(clazz)) {
                        localAbstractMediaService.set(abstractMediaService);
                    }
                }
        );
        return Optional.ofNullable(localAbstractMediaService.get());
    }
}
