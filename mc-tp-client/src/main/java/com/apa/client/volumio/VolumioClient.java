package com.apa.client.volumio;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.core.dto.media.VolumioMediaDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
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
    private static final List<String> EXCLUDE_ALBUM = List.of("*");
    private static final String TIDAL_ALBUMS_SEARCH_SECTION = "TIDAL Albums";
    @Value("${volumio.path}")
    private String volumioPath;

    @Value("#{'${volumio.paths}'.split(',')}")
    private String[] volumioPaths;


    public List<VolumioMediaDto> getVolumioLocalMedias(SaveDetemineStatus saveDetemineStatus) {
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
                list.addAll(getLocalTracks(uri, artist, album, saveDetemineStatus));
            }
        });
        return list;
    }



    public String getCurrentPlayedUri1() {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPaths[0])
                .path(GET_STATE)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject json = (JsonObject) JsonParser.parseString(response);
       return json.get("uri").getAsString().replace("mnt/", "music-library/");
    }
    public String getCurrentPlayedUri2() {
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPaths[1])
                .path(GET_STATE)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject json = (JsonObject) JsonParser.parseString(response);
       return json.get("uri").getAsString().replace("mnt/", "music-library/");
    }

    public interface SearchInterface {
        Collection<? extends VolumioMediaDto> getTidalTracks(String uri);
    }

    public Collection<? extends VolumioMediaDto> search(String artist, String album, SearchInterface searchInterface) {
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
                            .map(item -> searchInterface.getTidalTracks(item.getAsJsonObject().get("uri").getAsString()))
                            .filter(list -> !list.isEmpty())
                            .filter(list -> list.stream().anyMatch(t -> artist.equals(t.getAlbumArtist())))
                            .findFirst()
                            .orElse(Collections.EMPTY_LIST);
                }).orElse(Collections.EMPTY_LIST);
    }

    public Collection<? extends VolumioMediaDto> searchTrack(String artist, String album, SearchInterface searchInterface) {
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
                            .map(item -> searchInterface.getTidalTracks(item.getAsJsonObject().get("uri").getAsString()))
                            .filter(list -> !list.isEmpty())
                            .filter(list -> list.stream().anyMatch(t -> artist.equals(t.getAlbumArtist())))
                            .collect(Collectors.toList());

                }).flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .filter(m -> m.getTrackTitle().equals(album))
                .collect(Collectors.toList());
        return songsMatchingAlbum.stream().map(
                songMatchingAlbum -> searchInterface.getTidalTracks(songMatchingAlbum.getAlbumUri()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void refreshQueue(List<VolumioClientSong> list) {
        Client client = ClientBuilder.newClient();
        Arrays.stream(volumioPaths).forEach(path -> {
            Response response = client.target(path)
                    .path(CLEAR_QUEUE)
                    .queryParam("cmd", "clearQueue")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() != 200 ) {
                throw new RuntimeException("not able to clear queue");
            }
        });

        list.forEach(i ->
                Arrays.stream(volumioPaths).forEach(path -> {
                    Response res = client.target(path)
                            .path(ADD_TO_QUEUE)
                            .request(MediaType.APPLICATION_JSON)
                            .post(Entity.json(i));
                    if (res.getStatus() != 200) {
                        throw new RuntimeException("not able to add to queue");
                    }
                })
        );
    }

    private Collection<? extends VolumioMediaDto> getLocalTracks(String uri, String artist, String album, SaveDetemineStatus saveDetemineStatus) {
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

            saveDetemineStatus.saveDetemineStatus(uri, VolumioMedia.class.getName(), artist, album);
        }
        return mediaDtos;
    }

    public interface SaveDetemineStatus {
        void  saveDetemineStatus(String id, String clazz, String artist, String album);
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

    public List<VolumioMediaDto> extracted(String uri) throws InterruptedException {
        int retry = 0;
        boolean success = false;
        Client client = ClientBuilder.newClient();
        String response = client.target(volumioPath)
                .path(BROWSE)
                .queryParam("uri", uri)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<VolumioMediaDto> mediaDtos = new ArrayList<>();
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
                Thread.sleep(1000 * retry);
                retry++;
                if (retry > 5) {
                    throw throwable;
                }
            }
        }
        return mediaDtos;
    }

    public interface SearchInterfaceWithThreeParams {
        Collection<? extends VolumioMediaDto> getTidalTracks(String uri, String artist, String album);
    }

    public List<VolumioMediaDto> getVolumioTidalMedias(SearchInterfaceWithThreeParams searchInterface) {
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
            list.addAll(searchInterface.getTidalTracks(uri, artist, album));
        });
        return list;
    }


}
