package com.apa.producer.services.impl;

import com.apa.common.msg.InputMessage;
import com.apa.common.services.media.impl.plex.PlexMediaService;
import com.apa.common.services.media.impl.plex.PlexPerfectMatchFinder;
import com.apa.common.services.media.impl.tidal.TidalMediaService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class MatchProducer {

    @Autowired
    private PlexMediaService plexMediaService;

    @Autowired
    private TidalMediaService tidalMediaService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private PlexPerfectMatchFinder plexPerfectMatchFinder;

    @Autowired
    private KafkaTemplate<String, InputMessage> inputMessageTemplate;
}
