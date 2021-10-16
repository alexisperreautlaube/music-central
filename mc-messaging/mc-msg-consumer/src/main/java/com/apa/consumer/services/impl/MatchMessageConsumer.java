package com.apa.consumer.services.impl;

import com.apa.common.entities.media.PlexMedia;
import com.apa.common.entities.media.TidalMedia;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MatchStatus;
import com.apa.common.entities.util.MediaDistance;
import com.apa.common.entities.util.MediaReference;
import com.apa.common.msg.InputMessage;
import com.apa.common.msg.match.MatchMessageEvent;
import com.apa.common.msg.producer.MediaMediaData;
import com.apa.common.services.media.impl.plex.PlexMediaDistanceService;
import com.apa.common.services.media.impl.plex.PlexPerfectMatchFinder;
import com.apa.common.services.media.impl.tidal.TidalMediaDistanceService;
import com.apa.common.services.media.impl.tidal.TidalPerfectMatchFinder;
import com.apa.common.services.media.impl.volumio.VolumioMediaDistanceService;
import com.apa.common.services.media.impl.volumio.VolumioPerfectMatchFinder;
import com.apa.core.dto.media.PlexMediaDto;
import com.apa.core.dto.media.TidalMediaDto;
import com.apa.core.dto.media.VolumioMediaDto;
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

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MatchMessageConsumer implements ConsumerSeekAware {

    @Autowired
    private PlexPerfectMatchFinder plexPerfectMatchFinder;

    @Autowired
    private TidalPerfectMatchFinder tidalPerfectMatchFinder;

    @Autowired
    private VolumioPerfectMatchFinder volumioPerfectMatchFinder;

    @Autowired
    private PlexMediaDistanceService plexMediaDistanceService;

    @Autowired
    private TidalMediaDistanceService tidalMediaDistanceService;

    @Autowired
    private VolumioMediaDistanceService volumioMediaDistanceService;

    @Value("${spring.kafka.topic.match.resetOffset}")
    private Boolean resetOffset;

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        if (Boolean.TRUE.equals(resetOffset)) {
            assignments.forEach((t, o) -> callback.seekToBeginning(t.topic(), t.partition()));
        }
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.match.message}",
            containerFactory = "importMediaDtoKafkaListenerContainerFactory")
    public void doImport(InputMessage inputMessage) {
        MatchMessageEvent importMessageEvent = MatchMessageEvent.valueOf(inputMessage.getEvent());
        log.debug("matchMessageEvent={}", importMessageEvent);
        Gson gson = new Gson();
        switch (importMessageEvent) {
            case MATCH_PLEX_PERFECT:
                PlexMediaDto plexMediaDto = gson.fromJson(inputMessage.getData(), PlexMediaDto.class);
                PlexMedia plexMedia = PlexMediaMapper.toPlexMedia(plexMediaDto);
                List<PlexMedia> plexMatch = plexPerfectMatchFinder.findPlexMatch(plexMedia);
                List<TidalMedia> tidalMatch = plexPerfectMatchFinder.findTidalMatch(plexMedia);
                List<VolumioMedia> volumioMatch = plexPerfectMatchFinder.findVolumioMatch(plexMedia);
                if (plexMatch.isEmpty() && tidalMatch.isEmpty() && volumioMatch.isEmpty()) {
                    plexMediaDistanceService.save(MediaDistance.builder()
                            .from(MediaReference.builder()
                                    .id(plexMedia.getPlexId().toString())
                                    .clazz(plexMedia.getClass().getName())
                                    .build())
                            .to(null)
                            .artist(null)
                            .album(null)
                            .index(null)
                            .song(null)
                            .matchStatus(MatchStatus.NO_PERFECT_MATCH)
                            .build());
                    return;
                }
                break;
            case MATCH_TIDAL_PERFECT:
                TidalMediaDto tidalMediaDto = gson.fromJson(inputMessage.getData(), TidalMediaDto.class);
                TidalMedia tidalMedia = TidalMediaMapper.toTidalMedia(tidalMediaDto);
                plexMatch = tidalPerfectMatchFinder.findPlexMatch(tidalMedia);
                tidalMatch = tidalPerfectMatchFinder.findTidalMatch(tidalMedia);
                volumioMatch = tidalPerfectMatchFinder.findVolumioMatch(tidalMedia);
                if (plexMatch.isEmpty() && tidalMatch.isEmpty() && volumioMatch.isEmpty()) {
                    plexMediaDistanceService.save(MediaDistance.builder()
                            .from(MediaReference.builder()
                                    .id(tidalMedia.getTidalTrackId())
                                    .clazz(tidalMedia.getClass().getName())
                                    .build())
                            .to(null)
                            .artist(null)
                            .album(null)
                            .index(null)
                            .song(null)
                            .matchStatus(MatchStatus.NO_PERFECT_MATCH)
                            .build());
                    return;
                }
                break;
            case MATCH_VOLUMIO_PERFECT:
                VolumioMediaDto volumioMediaDto = gson.fromJson(inputMessage.getData(), VolumioMediaDto.class);
                VolumioMedia volumioMedia = VolumioMediaMapper.toVolumioMedia(volumioMediaDto);
                plexMatch = volumioPerfectMatchFinder.findPlexMatch(volumioMedia);
                tidalMatch = volumioPerfectMatchFinder.findTidalMatch(volumioMedia);
                volumioMatch = volumioPerfectMatchFinder.findVolumioMatch(volumioMedia);
                if (plexMatch.isEmpty() && tidalMatch.isEmpty() && volumioMatch.isEmpty()) {
                    plexMediaDistanceService.save(MediaDistance.builder()
                            .from(MediaReference.builder()
                                    .id(volumioMedia.getTrackUri())
                                    .clazz(volumioMedia.getClass().getName())
                                    .build())
                            .to(null)
                            .artist(null)
                            .album(null)
                            .index(null)
                            .song(null)
                            .matchStatus(MatchStatus.NO_PERFECT_MATCH)
                            .build());
                    return;
                }
                break;
            case MATCH_LEV_PLEX_PLEX:
                MediaMediaData mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                PlexMedia plexFrom = PlexMediaMapper.toPlexMedia((PlexMediaDto) mediaMediaData.getFrom());
                PlexMedia plexTo = PlexMediaMapper.toPlexMedia((PlexMediaDto) mediaMediaData.getTo());
                plexMediaDistanceService.distance(plexFrom, plexTo);
                break;
            case MATCH_LEV_PLEX_TIDAL:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                plexFrom = PlexMediaMapper.toPlexMedia((PlexMediaDto) mediaMediaData.getFrom());
                TidalMedia tidalTo = TidalMediaMapper.toTidalMedia((TidalMediaDto) mediaMediaData.getTo());
                plexMediaDistanceService.distance(plexFrom, tidalTo);
                break;
            case MATCH_LEV_PLEX_VOLUMIO:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                plexFrom = PlexMediaMapper.toPlexMedia((PlexMediaDto) mediaMediaData.getFrom());
                VolumioMedia volumioTo = VolumioMediaMapper.toVolumioMedia((VolumioMediaDto) mediaMediaData.getTo());
                plexMediaDistanceService.distance(plexFrom, volumioTo);
                break;
            case MATCH_LEV_TIDAL_PLEX:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                TidalMedia tidalFrom = TidalMediaMapper.toTidalMedia((TidalMediaDto) mediaMediaData.getFrom());
                plexTo = PlexMediaMapper.toPlexMedia((PlexMediaDto) mediaMediaData.getTo());
                tidalMediaDistanceService.distance(tidalFrom, plexTo);
                break;
            case MATCH_LEV_TIDAL_TIDAL:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                tidalFrom = TidalMediaMapper.toTidalMedia((TidalMediaDto) mediaMediaData.getFrom());
                tidalTo = TidalMediaMapper.toTidalMedia((TidalMediaDto) mediaMediaData.getTo());
                tidalMediaDistanceService.distance(tidalFrom, tidalTo);
                break;
            case MATCH_LEV_TIDAL_VOLUMIO:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                tidalFrom = TidalMediaMapper.toTidalMedia((TidalMediaDto) mediaMediaData.getFrom());
                volumioTo = VolumioMediaMapper.toVolumioMedia((VolumioMediaDto) mediaMediaData.getTo());
                tidalMediaDistanceService.distance(tidalFrom, volumioTo);
                break;
            case MATCH_LEV_VOLUMIO_PLEX:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                VolumioMedia volumioFrom = VolumioMediaMapper.toVolumioMedia((VolumioMediaDto) mediaMediaData.getFrom());
                plexTo = PlexMediaMapper.toPlexMedia((PlexMediaDto) mediaMediaData.getTo());
                volumioMediaDistanceService.distance(volumioFrom, plexTo);
                break;
            case MATCH_LEV_VOLUMIO_TIDAL:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                volumioFrom = VolumioMediaMapper.toVolumioMedia((VolumioMediaDto) mediaMediaData.getFrom());
                tidalFrom = TidalMediaMapper.toTidalMedia((TidalMediaDto) mediaMediaData.getFrom());
                volumioMediaDistanceService.distance(volumioFrom, tidalFrom);
                break;
            case MATCH_LEV_VOLUMIO_VOLUMIO:
                mediaMediaData = gson.fromJson(inputMessage.getData(), MediaMediaData.class);
                volumioFrom = VolumioMediaMapper.toVolumioMedia((VolumioMediaDto) mediaMediaData.getFrom());
                volumioTo = VolumioMediaMapper.toVolumioMedia((VolumioMediaDto) mediaMediaData.getTo());
                volumioMediaDistanceService.distance(volumioFrom, volumioTo);
                break;
            default:
                throw new RuntimeException("not implemented yet");
        }
    }
}
