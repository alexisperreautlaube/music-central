package com.apa.consumer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.match.MatchMessageEvent;
import com.apa.common.msg.producer.*;
import com.apa.common.services.media.impl.plex.PlexMediaDistanceService;
import com.apa.common.services.media.impl.tidal.TidalMediaDistanceService;
import com.apa.common.services.media.impl.volumio.VolumioMediaDistanceService;
import com.apa.events.mapper.PlexMediaMapper;
import com.apa.events.mapper.TidalMediaMapper;
import com.apa.events.mapper.VolumioMediaMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MatchMessageConsumer implements ConsumerSeekAware {

    @Autowired
    private PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    private TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    private VolumioMediaDistanceService volumioMediaDistanceService;

    @Value("${spring.kafka.topic.match.resetOffset}")
    private Boolean resetOffset;

    @Value("${spring.kafka.topic.match.message}")
    private String topic;

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        if (Boolean.TRUE.equals(resetOffset)) {
            assignments.forEach((t, o) -> {
                if (topic.equals(t.topic())) {
                    callback.seekToBeginning(t.topic(), t.partition());
                }
            });
        }
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.match.message}",
            containerFactory = "importMediaDtoKafkaListenerContainerFactory",
            concurrency = "20"
    )
    public void doImport(InputMessage inputMessage) {
        MatchMessageEvent importMessageEvent = MatchMessageEvent.valueOf(inputMessage.getEvent());
        log.debug("inputMessage={}", inputMessage);
        Gson gson = new Gson();
        switch (importMessageEvent) {
            case MATCH_LEV_PLEX_PLEX:
                log.debug("importMessageEvent start={}", importMessageEvent);
                PlexPlexMediaData mediaMediaData = gson.fromJson(inputMessage.getData(), PlexPlexMediaData.class);
                PlexMedia plexFrom = PlexMediaMapper.toPlexMedia(mediaMediaData.getFrom());
                PlexMedia plexTo = PlexMediaMapper.toPlexMedia(mediaMediaData.getTo());
                plexMediaDistanceService.distance(plexFrom, plexTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_PLEX_TIDAL:
                log.debug("importMessageEvent start={}", importMessageEvent);
                PlexTidalMediaData plexTidalMediaData = gson.fromJson(inputMessage.getData(), PlexTidalMediaData.class);
                plexFrom = PlexMediaMapper.toPlexMedia(plexTidalMediaData.getFrom());
                TidalMedia tidalTo = TidalMediaMapper.toTidalMedia(plexTidalMediaData.getTo());
                plexMediaDistanceService.distance(plexFrom, tidalTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_PLEX_VOLUMIO:
                log.debug("importMessageEvent start={}", importMessageEvent);
                PlexVolumioMediaData plexVolumioMediaData = gson.fromJson(inputMessage.getData(), PlexVolumioMediaData.class);
                plexFrom = PlexMediaMapper.toPlexMedia(plexVolumioMediaData.getFrom());
                VolumioMedia volumioTo = VolumioMediaMapper.toVolumioMedia(plexVolumioMediaData.getTo());
                plexMediaDistanceService.distance(plexFrom, volumioTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_TIDAL_PLEX:
                log.debug("importMessageEvent start={}", importMessageEvent);
                TidalPlexMediaData tidalPlexMediaData = gson.fromJson(inputMessage.getData(), TidalPlexMediaData.class);
                TidalMedia tidalFrom = TidalMediaMapper.toTidalMedia(tidalPlexMediaData.getFrom());
                plexTo = PlexMediaMapper.toPlexMedia(tidalPlexMediaData.getTo());
                tidalMediaDistanceService.distance(tidalFrom, plexTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_TIDAL_TIDAL:
                log.debug("importMessageEvent start={}", importMessageEvent);
                TidalTidalMediaData tidalTidalMediaData = gson.fromJson(inputMessage.getData(), TidalTidalMediaData.class);
                tidalFrom = TidalMediaMapper.toTidalMedia(tidalTidalMediaData.getFrom());
                tidalTo = TidalMediaMapper.toTidalMedia(tidalTidalMediaData.getTo());
                tidalMediaDistanceService.distance(tidalFrom, tidalTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_TIDAL_VOLUMIO:
                log.debug("importMessageEvent start={}", importMessageEvent);
                TidalVolumioMediaData tidalVolumioMediaData = gson.fromJson(inputMessage.getData(), TidalVolumioMediaData.class);
                tidalFrom = TidalMediaMapper.toTidalMedia(tidalVolumioMediaData.getFrom());
                volumioTo = VolumioMediaMapper.toVolumioMedia(tidalVolumioMediaData.getTo());
                tidalMediaDistanceService.distance(tidalFrom, volumioTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_VOLUMIO_PLEX:
                log.debug("importMessageEvent start={}", importMessageEvent);
                VolumioPlexMediaData volumioPlexMediaData = gson.fromJson(inputMessage.getData(), VolumioPlexMediaData.class);
                VolumioMedia volumioFrom = VolumioMediaMapper.toVolumioMedia(volumioPlexMediaData.getFrom());
                plexTo = PlexMediaMapper.toPlexMedia(volumioPlexMediaData.getTo());
                volumioMediaDistanceService.distance(volumioFrom, plexTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_VOLUMIO_TIDAL:
                log.debug("importMessageEvent start={}", importMessageEvent);
                VolumioTidalMediaData volumioTidalMediaData = gson.fromJson(inputMessage.getData(), VolumioTidalMediaData.class);
                volumioFrom = VolumioMediaMapper.toVolumioMedia(volumioTidalMediaData.getFrom());
                tidalFrom = TidalMediaMapper.toTidalMedia(volumioTidalMediaData.getTo());
                volumioMediaDistanceService.distance(volumioFrom, tidalFrom);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            case MATCH_LEV_VOLUMIO_VOLUMIO:
                log.debug("importMessageEvent start={}", importMessageEvent);
                VolumioVolumioMediaData volumioVolumioMediaData = gson.fromJson(inputMessage.getData(), VolumioVolumioMediaData.class);
                volumioFrom = VolumioMediaMapper.toVolumioMedia(volumioVolumioMediaData.getFrom());
                volumioTo = VolumioMediaMapper.toVolumioMedia(volumioVolumioMediaData.getTo());
                volumioMediaDistanceService.distance(volumioFrom, volumioTo);
                log.debug("importMessageEvent end={}", importMessageEvent);
                break;
            default:
                throw new RuntimeException("not implemented yet");
        }
    }
}
