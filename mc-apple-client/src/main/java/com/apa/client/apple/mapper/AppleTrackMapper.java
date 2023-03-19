package com.apa.client.apple.mapper;

import com.apa.client.apple.entity.AppleTrack;
import com.github.pireba.applescript.AppleScriptException;
import com.github.pireba.applescript.AppleScriptObject;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class AppleTrackMapper {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm:ss aaa", Locale.ENGLISH); // Monday,June 7,2004 at 8:00:00 AM

    private enum Type {
        STRING,
        LONG,
        INTEGER,
        LOCAL_DATE_TIME,
        DURATION,
        DOUBLE,
        SHORT,
        BOOLEAN
    }
    public static final String CLASS = "class";
    public static final String ID = "id";
    public static final String INDEX = "index";
    public static final String NAME = "name";
    public static final String PERSISTENT_ID = "persistent ID";
    public static final String DATABASE_ID = "database ID";
    public static final String DATE_ADDED = "date added";
    public static final String TIME = "time";
    public static final String DURATION = "duration";
    public static final String ARTIST = "artist";
    public static final String ALBUM_ARTIST = "album artist";
    public static final String COMPOSER = "composer";
    public static final String ALBUM = "album";
    public static final String GENRE = "genre";
    public static final String BIT_RATE = "bit rate";
    public static final String SAMPLE_RATE = "sample rate";
    public static final String TRACK_COUNT = "track count";
    public static final String TRACK_NUMBER = "track number";
    public static final String DISC_COUNT = "disc count";
    public static final String DISC_NUMBER = "disc number";
    public static final String SIZE = "size";
    public static final String VOLUME_ADJUSTMENT = "volume adjustment";
    public static final String YEAR = "year";
    public static final String COMMENT = "comment";
    public static final String EQ = "EQ";
    public static final String KIND = "kind";
    public static final String MEDIA_KIND = "media kind";
    public static final String MODIFICATION_DATE = "modification date";
    public static final String ENABLED = "enabled";
    public static final String START = "start";
    public static final String FINISH = "finish";
    public static final String PLAYED_COUNT = "played count";

    public static final String PLAYED_DATE = "played date";
    public static final String SKIPPED_COUNT = "skipped count";

    public static final String SKIPPED_DATE = "skipped date";
    public static final String COMPILATION = "compilation";
    public static final String RATING = "rating";
    public static final String BPM = "bpm";
    public static final String GROUPING = "grouping";
    public static final String BOOKMARKABLE = "bookmarkable";
    public static final String BOOKMARK = "bookmark";
    public static final String SHUFFLABLE = "shufflable";
    public static final String LYRICS = "lyrics";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    public static final String EPISODE_NUMBER = "episode number";
    public static final String UNPLAYED = "unplayed";
    public static final String SORT_NAME = "sort name";
    public static final String SORT_ALBUM = "sort album";
    public static final String SORT_ARTIST = "sort artist";
    public static final String SORT_COMPOSER = "sort composer";
    public static final String SORT_ALBUM_ARTIST = "sort album artist";
    public static final String RELEASE_DATE = "release date";
    public static final String LOVED = "loved";
    public static final String DISLIKED = "disliked";
    public static final String ALBUM_LOVED = "album loved";
    public static final String ALBUM_DISLIKED = "album disliked";
    public static final String CLOUD_STATUS = "cloud status";
    public static final String WORK = "work";
    public static final String MOVEMENT = "movement";
    public static final String MOVEMENT_NUMBER = "movement number";
    public static final String MOVEMENT_COUNT = "movement count";
    
    private static Map<String, Type> attributeMapping  = new HashMap<>() {{
        put(CLASS, Type.STRING);
        put(ID, Type.LONG);
        put(INDEX, Type.SHORT);
        put(NAME, Type.STRING);
        put(PERSISTENT_ID, Type.STRING);
        put(DATABASE_ID, Type.LONG);
        put(DATE_ADDED, Type.LOCAL_DATE_TIME);
        put(TIME, Type.DURATION);
        put(DURATION, Type.DOUBLE);
        put(ARTIST, Type.STRING);
        put(ALBUM_ARTIST, Type.STRING);
        put(COMPOSER, Type.STRING);
        put(ALBUM, Type.STRING);
        put(GENRE, Type.STRING);
        put(BIT_RATE, Type.INTEGER);
        put(SAMPLE_RATE, Type.INTEGER);
        put(TRACK_COUNT, Type.SHORT);
        put(TRACK_NUMBER, Type.SHORT);
        put(DISC_COUNT, Type.SHORT);
        put(DISC_NUMBER, Type.SHORT);
        put(SIZE, Type.INTEGER);
        put(VOLUME_ADJUSTMENT, Type.INTEGER);
        put(YEAR, Type.SHORT);
        put(COMMENT, Type.STRING);
        put(EQ, Type.STRING);
        put(KIND, Type.STRING);
        put(MEDIA_KIND, Type.STRING);
        put(MODIFICATION_DATE, Type.LOCAL_DATE_TIME);
        put(ENABLED, Type.BOOLEAN);
        put(START, Type.DOUBLE);
        put(FINISH, Type.DOUBLE);
        put(PLAYED_COUNT, Type.INTEGER);
        put(PLAYED_DATE, Type.LOCAL_DATE_TIME);
        put(SKIPPED_COUNT, Type.INTEGER);
        put(SKIPPED_DATE, Type.LOCAL_DATE_TIME);
        put(COMPILATION, Type.BOOLEAN);
        put(RATING, Type.SHORT);
        put(BPM, Type.INTEGER);
        put(GROUPING, Type.STRING);
        put(BOOKMARKABLE, Type.BOOLEAN);
        put(BOOKMARK, Type.DOUBLE);
        put(SHUFFLABLE, Type.BOOLEAN);
        put(LYRICS, Type.STRING);
        put(CATEGORY, Type.STRING);
        put(DESCRIPTION, Type.STRING);
        put(EPISODE_NUMBER, Type.SHORT);
        put(UNPLAYED, Type.BOOLEAN);
        put(SORT_NAME, Type.STRING);
        put(SORT_ALBUM, Type.STRING);
        put(SORT_ARTIST, Type.STRING);
        put(SORT_COMPOSER, Type.STRING);
        put(SORT_ALBUM_ARTIST, Type.STRING);
        put(RELEASE_DATE, Type.LOCAL_DATE_TIME);
        put(LOVED, Type.BOOLEAN);
        put(DISLIKED, Type.BOOLEAN);
        put(ALBUM_LOVED, Type.BOOLEAN);
        put(ALBUM_DISLIKED, Type.BOOLEAN);
        put(CLOUD_STATUS, Type.STRING);
        put(WORK, Type.STRING);
        put(MOVEMENT, Type.STRING);
        put(MOVEMENT_NUMBER, Type.INTEGER);
        put(MOVEMENT_COUNT, Type.SHORT);
    }};

    public static AppleTrack fromAppleScriptList(AppleScriptObject appleScriptObject) {
        try {
            AppleTrack track = new AppleTrack();
            appleScriptObject.getList().stream().forEach(object -> {
                try {
                    setAttribute(track, object);
                } catch (AppleScriptException e) {
                    throw new RuntimeException(e);
                }
            });
            return track;
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppleTrack fromAppleScriptMap(AppleScriptObject appleScriptObject) {
        try {
            AppleTrack track = new AppleTrack();
            appleScriptObject.getMap().entrySet().stream().forEach(entry -> {
                setAttribute(track, entry.getValue(), entry.getKey());
            });
            return track;
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setAttribute(AppleTrack appleTrack, AppleScriptObject appleScriptObject) throws AppleScriptException {
        String attributeName = getAttributeName(appleScriptObject);
        setAttribute(appleTrack, appleScriptObject, attributeName);
    }

    private static void setAttribute(AppleTrack appleTrack, AppleScriptObject appleScriptObject, String attributeName) {
        if (CLASS.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setClazz((String) value);
        } else if (ID.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setId((Long) value);
        } else if (INDEX.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setIndex((Short) value);
        } else if (NAME.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setName((String) value);
        } else if (PERSISTENT_ID.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setPersistent((String) value);
        } else if (DATABASE_ID.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDatabase((Long) value);
        } else if (DATE_ADDED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDateAdded((LocalDateTime) value);
        } else if (TIME.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setTime((Duration) value);
        } else if (DURATION.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDuration((Double) value);
        } else if (ARTIST.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setArtist((String) value);
        } else if (ALBUM_ARTIST.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setAlbumArtist((String) value);
        } else if (COMPOSER.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setComposer((String) value);
        } else if (ALBUM.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setAlbum((String) value);
        } else if (GENRE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setGenre((String) value);
        } else if (BIT_RATE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setBitRate((Integer) value);
        } else if (SAMPLE_RATE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSampleRate((Integer) value);
        } else if (TRACK_COUNT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setTrackCount((Short) value);
        } else if (TRACK_NUMBER.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setTrackNumber((Short) value);
        } else if (DISC_COUNT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDiscCount((Short) value);
        } else if (DISC_NUMBER.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDiscNumber((Short) value);
        } else if (SIZE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSize((Integer) value);
        } else if (VOLUME_ADJUSTMENT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setVolumeAdjustment((Integer) value);
        } else if (YEAR.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setYear((Short) value);
        } else if (COMMENT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setComment((String) value);
        } else if (EQ.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setEq((String) value);
        } else if (KIND.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setKind((String) value);
        } else if (MEDIA_KIND.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setMediaKind((String) value);
        } else if (MODIFICATION_DATE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setModificationDate((LocalDateTime) value);
        } else if (ENABLED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setEnabled((Boolean) value);
        } else if (START.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setStart((Double) value);
        } else if (FINISH.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setFinish((Double) value);
        } else if (PLAYED_COUNT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setPlayedCount((Integer) value);
        } else if (PLAYED_DATE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setPlayedDate((LocalDateTime) value);
        } else if (SKIPPED_COUNT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSkippedCount((Integer) value);
        } else if (COMPILATION.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setCompilation((Boolean) value);
        } else if (RATING.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setRating((Short) value);
        } else if (BPM.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setBpm((Integer) value);
        } else if (GROUPING.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setGrouping((String) value);
        } else if (BOOKMARKABLE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setBookmarkable((Boolean) value);
        } else if (BOOKMARK.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setBookmark((Double) value);
        } else if (SHUFFLABLE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setShufflable((Boolean) value);
        } else if (LYRICS.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setLyrics((String) value);
        } else if (CATEGORY.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setCategory((String) value);
        } else if (DESCRIPTION.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDescription((String) value);
        } else if (EPISODE_NUMBER.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setEpisodeNumber((Short) value);
        } else if (UNPLAYED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setUnplayed((Boolean) value);
        } else if (SORT_NAME.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSortName((String) value);
        } else if (SORT_ALBUM.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSortAlbum((String) value);
        } else if (SORT_ARTIST.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSortArtist((String) value);
        } else if (SORT_COMPOSER.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSortComposer((String) value);
        } else if (SORT_ALBUM_ARTIST.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSortAlbumArtist((String) value);
        } else if (RELEASE_DATE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setReleaseDate((LocalDateTime) value);
        } else if (LOVED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setLoved((Boolean) value);
        } else if (DISLIKED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setDisliked((Boolean) value);
        } else if (ALBUM_LOVED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setAlbumLoved((Boolean) value);
        } else if (ALBUM_DISLIKED.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setAlbumDisliked((Boolean) value);
        } else if (CLOUD_STATUS.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setCloudStatus((String) value);
        } else if (WORK.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setWork((String) value);
        } else if (MOVEMENT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setMovement((String) value);
        } else if (MOVEMENT_NUMBER.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setMovementNumber((Integer) value);
        } else if (MOVEMENT_COUNT.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setMovementCount((Short) value);
        } else if (SKIPPED_DATE.equals(attributeName)) {
            Object value = extractValue(attributeName, appleScriptObject);
            appleTrack.setSkippedDate((LocalDateTime) value);
        } else {
            throw new RuntimeException("ish");
        }
    }

    private static Object extractValue(String attributeName, AppleScriptObject appleScriptObject) {
        Type type = attributeMapping.get(attributeName);
        try {
            String value = appleScriptObject.getString().replace(attributeName + ":", "");
            if (value.startsWith("\"")) {
                value = value.substring(1);
            }
            if (value.endsWith("\"")) {
                value = value.substring(0, value.length() -1);
            }
            if (StringUtils.isBlank(value)) {
                return null;
            }
            if (type.equals(Type.STRING)) {
                return value;
            } else if (type.equals(Type.LONG)) {
                return Long.valueOf(value);
            } else if (type.equals(Type.INTEGER)) {
                return Integer.valueOf(value);
            } else if (type.equals(Type.LOCAL_DATE_TIME)) {
                value = value.replace("at ", "");
                value = value.replace("date ", "");
                value = value.replace("\"", "");
                try {
                    Date date = DATE_FORMAT.parse(value);

                    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                } catch (NumberFormatException n) {
                    return null;
                }
            } else if (type.equals(Type.DURATION)) {
                String[] split = value.split(":");
                int min = Integer.valueOf(split[0].replace("\"", ""));
                int sec = Integer.valueOf(split[1].replace("\"", ""));
                return Duration.ofSeconds(min * 60 + sec);
            } else if (type.equals(Type.DOUBLE)) {
                return Double.valueOf(value);
            } else if (type.equals(Type.SHORT)) {
                return Short.valueOf(value);
            } else if (type.equals(Type.BOOLEAN)) {
                return Boolean.valueOf(value);
            }
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("ouff");
    }

    private static String getAttributeName(AppleScriptObject object) {
        return Arrays.stream(object.toString().split(":")).findFirst().get();
    }
}
