package com.apa.client.volumio;

import com.apa.common.entities.enums.MediaErrorStatus;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.services.media.AvailableMediasService;
import com.apa.common.services.media.impl.MediaErrorService;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class VolumioClient {

    private static final String CLEAR_QUEUE = "/commands";
    private static final String ADD_TO_QUEUE = "/addToQueue";
    private static final String GET_STATE = "/getState";
    private static final String BROWSE = "/browse";
    private static final String SEARCH = "/search";
    private static final String BROWSE_ALBUM = "albums://";
    private static final String BROWSE_TIDAL = "tidal://mymusic/albums/az";
    private static final List<String> EXCLUDE_TRACK_TYPE = List.of("avi", "mpg", "mov");
    private static final List<String> EXCLUDE_ALBUM = List.of("*");
    private static final String TIDAL_ALBUMS_SEARCH_SECTION = "TIDAL Albums";
    @Value("${volumio.path}")
    private String volumioPath;

    @Autowired
    private AvailableMediasService availableMediasService;

    @Autowired
    private VolumioMediaService volumioMediaService;

    @Autowired
    private MediaErrorService mediaErrorService;

    public String getCurrentPlayedUri() {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(GET_STATE)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject json = (JsonObject) JsonParser.parseString(response);
       return json.get("uri").getAsString().replace("mnt/", "music-library/");
    }

    public Collection<? extends VolumioMediaDto> search(String artist, String album) {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(SEARCH)
                .queryParam("query", artist)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        JsonObject json = (JsonObject) JsonParser.parseString(response);
        JsonObject navigation = json.get("navigation").getAsJsonObject();
        JsonArray lists = navigation.getAsJsonArray("lists");
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(lists.iterator(), Spliterator.ORDERED), false)
                .filter(subList -> TIDAL_ALBUMS_SEARCH_SECTION.equalsIgnoreCase(subList.getAsJsonObject().get("title").getAsString()))
                .findFirst()
                .map(subList -> {
                    JsonArray items = subList.getAsJsonObject().get("items").getAsJsonArray();
                    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(items.iterator(), Spliterator.ORDERED), false)
                            .filter(item -> album.equalsIgnoreCase(item.getAsJsonObject().get("title").getAsString()))
                            .map(item -> getTidalTracks(item.getAsJsonObject().get("uri").getAsString()))
                            .filter(list -> !list.isEmpty())
                            .filter(list -> list.stream().anyMatch(t -> artist.equals(t.getAlbumArtist())))
                            .findFirst()
                            .orElse(Collections.EMPTY_LIST);
                }).orElse(Collections.EMPTY_LIST);
    }

    public Collection<? extends VolumioMediaDto> searchTrack(String artist, String album) {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(SEARCH)
                .queryParam("query", artist)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        JsonObject json = (JsonObject) JsonParser.parseString(response);
        JsonObject navigation = json.get("navigation").getAsJsonObject();
        JsonArray lists = navigation.getAsJsonArray("lists");
        List<? extends VolumioMediaDto> songsMatchingAlbum = StreamSupport.stream(Spliterators.spliteratorUnknownSize(lists.iterator(), Spliterator.ORDERED), false)
                .filter(subList -> TIDAL_ALBUMS_SEARCH_SECTION.equalsIgnoreCase(subList.getAsJsonObject().get("title").getAsString()))
                .map(subList -> {
                    JsonArray items = subList.getAsJsonObject().get("items").getAsJsonArray();
                    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(items.iterator(), Spliterator.ORDERED), false)
                            .map(item -> getTidalTracks(item.getAsJsonObject().get("uri").getAsString()))
                            .filter(list -> !list.isEmpty())
                            .filter(list -> list.stream().anyMatch(t -> artist.equals(t.getAlbumArtist())))
                            .collect(Collectors.toList());

                }).flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .filter(m -> m.getTrackTitle().equals(album))
                .collect(Collectors.toList());
        return songsMatchingAlbum.stream().map(
                songMatchingAlbum -> getTidalTracks(songMatchingAlbum.getAlbumUri()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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
                String uri = a.getAsJsonObject().get("uri").getAsString();
                String artist = getNotNullString(a.getAsJsonObject(), "artist");
                String album = getNotNullString(a.getAsJsonObject(), "title");
                if (!EXCLUDE_ALBUM.contains(album.toLowerCase())) {
                    list.addAll(getLocalTracks(uri, artist, album));
                }
            });

        List<VolumioMediaDto> collect = list.stream()
                .filter(v -> !volumioMediaService.existAndEquals(VolumioMediaMapper.toVolumioMedia(v)))
                .filter(v -> !EXCLUDE_TRACK_TYPE.contains(v.getTrackType().toLowerCase()))
                .collect(Collectors.toList());
        return collect;
    }

    private Collection<? extends VolumioMediaDto> getLocalTracks(String uri, String artist, String album) {
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
                            .trackNumber(getNotNullString(m.getAsJsonObject(), "trackNumber"))
                            .trackTitle(getNotNullString(m.getAsJsonObject(), "title"))
                            .addedDate(LocalDate.now())
                            .build())
            );
        } catch (Throwable e) {
            log.error("response={}, uri={}, artist={}, album={}, uri={}", response, uri, artist, album, volumioPath + BROWSE + "?uri=" + uri);
            mediaErrorService.saveDetemineStatus(uri, VolumioMedia.class.getName(), artist, album);
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
        return getTidalTracks(uri, null, null);
    }

    private Collection<? extends VolumioMediaDto> getTidalTracks(String uri, String artist, String album) {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(BROWSE)
                .queryParam("uri", uri)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<VolumioMediaDto> mediaDtos = new ArrayList<>();
        try {
            int retry = 0;
            boolean success = false;
            while (!success) {
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
                    success = true;
                } catch (Throwable throwable) {
                    Thread.sleep(3000);
                    retry++;
                    if (retry > 3) {
                        throw throwable;
                    }
                }
            }
            mediaErrorService.remove(uri, VolumioMedia.class.getName());
        } catch (Throwable e) {
            if (artist != null && album != null) {
                if (mediaErrorService.doesNotExistOrExistWithErrorStatus(uri, VolumioMedia.class.getName())) {
                    boolean existingAlbumMatch = existingAlbumMatch(uri, artist, album, response);
                    if (!existingAlbumMatch) {
                        boolean existingTrackMatch = existingTrackMatch(uri, artist, album, response);
                        if (!existingTrackMatch) {
                            if (mediaErrorService.doesNotExistOrExistWithStatusElse(uri, VolumioMedia.class.getName(), MediaErrorStatus.ERROR)) {
                                log.error("response={}, uri={}, artist={}, album={}, uri={}", response, uri, artist, album, volumioPath + BROWSE + "?uri=" + uri);
                                mediaErrorService.save(uri, VolumioMedia.class.getName(), artist, album, MediaErrorStatus.ERROR);
                            }
                        }
                    }
                }

            } else {
                log.debug("response={}, uri={}, artist={}, album={}, uri={}", response, uri, artist, album, volumioPath + BROWSE + "?uri=" + uri);
            }
        }
        return mediaDtos;
    }

    private boolean existingTrackMatch(String uri, String artist, String album, String response) {
        Collection<? extends VolumioMediaDto> search = searchTrack(artist, album);
        if (!search.isEmpty()) {
            if (!volumioMediaService.existByAlbumUri(search.stream().findFirst().get().getAlbumUri())) {
                search.stream()
                        .forEach(m -> volumioMediaService.save(VolumioMediaMapper.toVolumioMedia(m)));
            }
            mediaErrorService.save(uri, VolumioMedia.class.getName(), artist, album, MediaErrorStatus.ERROR_WITH_ALBUM_AS_TRACK_IN_ALBUM);
            return true;
        }
        return false;
    }
    private boolean existingAlbumMatch(String uri, String artist, String album, String response) {
        Collection<? extends VolumioMediaDto> search = search(artist, album);
        if (!search.isEmpty()) {
            if (!volumioMediaService.existByAlbumUri(search.stream().findFirst().get().getAlbumUri())) {
                search.stream()
                        .forEach(m -> volumioMediaService.save(VolumioMediaMapper.toVolumioMedia(m)));
            }
            mediaErrorService.save(uri, VolumioMedia.class.getName(), artist, album, MediaErrorStatus.ERROR_WITH_REPLACEMENT);
            return true;
        }
        return false;
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
            String artist = getNotNullString(a.getAsJsonObject(), "artist");
            String album = getNotNullString(a.getAsJsonObject(), "title");
            list.addAll(getTidalTracks(uri, artist, album));
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
