package com.apa.consumer.services.impl;

import com.apa.common.repositories.PlexMediaRepository;
import com.apa.common.repositories.TidalMediaRepository;
import com.apa.consumer.AbstractConsumerIT;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
class MediaImporterIT extends AbstractConsumerIT {

    @Autowired
    private TidalMediaRepository tidalMediaRepository;

    @Autowired
    private PlexMediaRepository plexMediaRepository;


}