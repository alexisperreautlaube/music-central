package com.apa.client.volumio;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.services.media.AvailableMediasService;
import com.apa.common.services.media.impl.volumio.VolumioMediaService;
import com.apa.core.dto.media.VolumioMediaDto;
import com.apa.events.mapper.VolumioMediaMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VolumioClient {

    private static final String CLEAR_QUEUE = "/commands";
    private static final String ADD_TO_QUEUE = "/addToQueue";
    private static final String GET_STATE = "/getState";
    private static final String BROWSE = "/browse";
    private static final String BROWSE_ALBUM = "albums://";
    private static final String BROWSE_TIDAL = "tidal://mymusic/albums/az";
    private static final List<String> EXCLUDE_TRACK_TYPE = List.of("avi", "mpg", "mov");
    private static final List<String> EXCLUDE_ALBUM = List.of("*");
    @Value("${volumio.path}")
    private String volumioPath;

    @Autowired
    private AvailableMediasService availableMediasService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    public String getCurrentPlayedUri() {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(GET_STATE)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject json = (JsonObject) JsonParser.parseString(response);
       return json.get("uri").getAsString();
    }

    public void refreshQueue() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(volumioPath)
                .path(CLEAR_QUEUE)
                .queryParam("cmd", "clearQueue")
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatus() != 200 ) {
            throw new RuntimeException("not able to clear queue");
        }
        getList().forEach(i -> {
            Response res = client.target(volumioPath)
                    .path(ADD_TO_QUEUE)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(i));
            if (res.getStatus() != 200 ) {
                throw new RuntimeException("not able to clear queue");
            }
        });

    }

    public List<VolumioMediaDto> getNotSavedVolumioLocalAlbum() {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(BROWSE)
                .queryParam("uri", BROWSE_ALBUM)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject json = (JsonObject) JsonParser.parseString(response);
        JsonObject navigation = json.get("navigation").getAsJsonObject();
        JsonArray lists = navigation.getAsJsonArray("lists");
        JsonArray albums = lists.get(0).getAsJsonObject().getAsJsonArray("items");
        List<VolumioMediaDto> list = new ArrayList<>();
        albums.forEach(a -> {
            list.addAll(getLocalTracks(a.getAsJsonObject().get("uri").getAsString()));
        });

        List<VolumioMediaDto> collect = list.stream()
                .filter(v -> !volumioMediaService.existAndEquals(VolumioMediaMapper.toVolumioMedia(v)))
                .filter(v -> !EXCLUDE_TRACK_TYPE.contains(v.getTrackType().toLowerCase()))
                .filter(v -> !EXCLUDE_ALBUM.contains(v.getAlbumTitle().toLowerCase()))
                .collect(Collectors.toList());
        return collect;
    }

    private Collection<? extends VolumioMediaDto> getLocalTracks(String uri) {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(BROWSE)
                .queryParam("uri", uri)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        List<VolumioMediaDto> mediaDtos = new ArrayList<>();
        try {
            JsonObject json = (JsonObject) JsonParser.parseString(response);

            JsonObject navigation = json.get("navigation").getAsJsonObject();
            JsonObject albumInfo = navigation.get("info").getAsJsonObject();
            JsonArray lists = navigation.getAsJsonArray("lists");
            JsonArray tracks = lists.get(0).getAsJsonObject().getAsJsonArray("items");

            tracks.forEach(m ->
                    mediaDtos.add(VolumioMediaDto.builder()
                                    .albumTitle(getNotNullString(albumInfo.getAsJsonObject(), "album"))
                                    .albumUri(getNotNullString(albumInfo.getAsJsonObject(), "uri"))
                                    .albumTrackType(getNotNullString(albumInfo.getAsJsonObject(), "trackType"))
                                    .albumArtist(getNotNullString(albumInfo.getAsJsonObject(), "artist"))
                                    .trackType(getNotNullString(m.getAsJsonObject(), "trackType"))
                                    .trackArtist(getNotNullString(m.getAsJsonObject(), "artist"))
                                    .trackUri(getNotNullString(m.getAsJsonObject(), "uri"))
                                    .trackDuration(getNotNullString(m.getAsJsonObject(), "duration"))
                                    .trackNumber(getNotNullString(m.getAsJsonObject(),"trackNumber"))
                                    .trackTitle(getNotNullString(m.getAsJsonObject(),"title"))
                                    .addedDate(LocalDate.now())
                            .build())
            );
        } catch (Throwable e) {
            log.error("response={}, uri={}", response, uri);
        }
        return mediaDtos;
    }

    private String getNotNullString(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        if (jsonElement == null) {
            return "";
        }
        if (jsonElement.isJsonNull()) {
            return "";
        }
        return jsonElement.getAsString();
    }


    private Collection<? extends VolumioMediaDto> getTidalTracks(String uri) {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(BROWSE)
                .queryParam("uri", uri)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<VolumioMediaDto> mediaDtos = new ArrayList<>();
        try {
            JsonObject json = (JsonObject) JsonParser.parseString(response);
            JsonObject navigation = json.get("navigation").getAsJsonObject();
            JsonArray lists = navigation.getAsJsonArray("lists");
            JsonArray tracks = lists.get(0).getAsJsonObject().getAsJsonArray("items");

            tracks.forEach(m ->
                    mediaDtos.add(VolumioMediaDto.builder()
                            .albumTitle(getNotNullString(m.getAsJsonObject(), "album"))
                            .albumUri(uri)
                            .albumTrackType("tidal")
                            .albumArtist(getNotNullString(m.getAsJsonObject(), "artist"))
                            .albumAudioQuality(getNotNullString(m.getAsJsonObject(), "audioQuality"))
                            .trackType(getNotNullString(m.getAsJsonObject(), "trackType"))
                            .trackArtist(getNotNullString(m.getAsJsonObject(), "artist"))
                            .trackUri(getNotNullString(m.getAsJsonObject(), "uri"))
                            .trackDuration(getNotNullString(m.getAsJsonObject(), "duration"))
                            .trackNumber(getNotNullString(m.getAsJsonObject(), "track_number"))
                            .trackAudioQuality(getNotNullString(m.getAsJsonObject(), "audioQuality"))
                            .trackTitle(getNotNullString(m.getAsJsonObject(), "title"))
                            .build())
            );
        } catch (Throwable e) {
            log.error("response={}, uri={}", response, uri);
        }
        return mediaDtos;
    }

    public List<VolumioMediaDto> getNotSavedVolumioTidalAlbum() {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(BROWSE)
                .queryParam("uri", BROWSE_TIDAL)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject json = (JsonObject) JsonParser.parseString(response);
        JsonObject navigation = json.get("navigation").getAsJsonObject();
        JsonArray lists = navigation.getAsJsonArray("lists");
        JsonArray albums = lists.get(1).getAsJsonObject().getAsJsonArray("items");
        List<VolumioMediaDto> list = new ArrayList<>();
        albums.forEach(a -> {
                String uri = a.getAsJsonObject().get("uri").getAsString();
            if (!volumioMediaService.existByAlbumUri(uri)) {
                list.addAll(getTidalTracks(uri));
            }
        });

        List<VolumioMediaDto> collect = list.stream()
                .filter(v -> !volumioMediaService.existAndEquals(VolumioMediaMapper.toVolumioMedia(v)))
                .collect(Collectors.toList());
        return collect;
    }

    private List<VolumioClientSong> getList() {
        List<String> triageIdList = availableMediasService.createTriageIdList();
        List<VolumioClientSong> volumioClientSongs = new ArrayList<>();
        triageIdList.stream().forEach(id -> {
            VolumioMedia byId = volumioMediaService.findById(id);
            volumioClientSongs.add(VolumioClientSong.builder()
                    .uri(byId.getTrackUri())
                    .service(byId.getAlbumTrackType().equals("tidal") ? "tidal" : "mpd")
                    .title(byId.getTrackTitle())
                    .artist(byId.getTrackArtist())
                    .album(byId.getAlbumTitle())
                    .type("song")
                    .trackNumber(0)
                    .duration(byId.getTrackDuration() != null ? Integer.valueOf(byId.getTrackDuration()) : 0)
                    .trackType(byId.getTrackType())
                    .build());
        });

        return volumioClientSongs;
    }
}
