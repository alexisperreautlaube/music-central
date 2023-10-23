package com.apa.client.apple;

import com.apa.client.apple.entity.AppleEqualizer;
import com.apa.client.apple.entity.AppleEqualizerAndBands;
import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.mapper.AppleTrackMapper;
import com.github.pireba.applescript.AppleScript;
import com.github.pireba.applescript.AppleScriptException;
import com.github.pireba.applescript.AppleScriptObject;
import com.google.common.base.Charsets;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AppleClient {

    private static final String TRIAGE = "triage";
    private static final String BEST_OF = "bestOf";
    @Value("classpath:script/playPause")
    private Resource playPauseScript;

    @Value("classpath:script/getCurrentTrack")
    private Resource getCurrentTrack;

    @Value("classpath:script/getCurrentEq")
    private Resource getCurrentEq;

    @Value("classpath:script/search")
    private Resource search;

    @Value("classpath:script/getAllTracksIds")
    private Resource getAllTracks;

    @Value("classpath:script/getPropertiesOfTrack")
    private Resource getPropertiesOfTrack;

    @Value("classpath:script/getPropertiesOfTracks")
    private Resource getPropertiesOfTracks;

    @Value("classpath:script/emptyPlaylist")
    private Resource emptyPlaylist;

    @Value("classpath:script/addTrackToPlaylist")
    private Resource addTrackToPlayList;

    @Value("classpath:script/addTracksToPlaylist")
    private Resource addTracksToPlayList;

    @Value("classpath:script/setRating")
    private Resource setRating;

    @Value("classpath:script/setRatingOfCurrentTrack")
    private Resource setRatingOfCurrentTrack;

    @Value("classpath:script/setRatingOfCurrentTrackAndSkip")
    private Resource setRatingOfCurrentTrackAndSkip;

    @Value("classpath:script/setEqualizer")
    private Resource setEqualizer;

    @Value("classpath:script/createAndAssignEq")
    private Resource createAndAssignEq;

    @Value("classpath:script/getEqualizerList")
    private Resource getEqualizerList;

    public void createAndAssignEq() {
        try {
            String text = new String(createAndAssignEq.getInputStream().readAllBytes(), Charsets.UTF_8);
            AppleScript as = new AppleScript(text);
            as.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }
    public void setEqualizer(String index) {
        try {
            String text = new String(setEqualizer.getInputStream().readAllBytes(), Charsets.UTF_8);
            text = text.replace("${eq_index}", index);
            AppleScript as = new AppleScript(text);
            as.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRatingOfCurrentTrackAndSkip(int rating) {
        try {
            String text = new String(setRatingOfCurrentTrackAndSkip.getInputStream().readAllBytes(), Charsets.UTF_8);
            text = text.replace("{$rating}", "" + rating);
            AppleScript as = new AppleScript(text);
            as.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRatingOfCurrentTrack(int rating) {
        try {
            String text = new String(setRatingOfCurrentTrack.getInputStream().readAllBytes(), Charsets.UTF_8);
            text = text.replace("{$rating}", "" + rating);
            AppleScript as = new AppleScript(text);
            as.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRating(int rating, long id) {
        try {
            String text = new String(setRating.getInputStream().readAllBytes(), Charsets.UTF_8);
            text = text.replace("${track_id}", "" + id);
            text = text.replace("{$rating}", "" + rating);
            //log.info("setRating script={}", text);
            AppleScript as = new AppleScript(text);
            as.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void playPause() {
        try {
            String text = new String(playPauseScript.getInputStream().readAllBytes(), Charsets.UTF_8);
            AppleScript as = new AppleScript(text);
            String result = as.executeAsString();
            log.info(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public AppleTrack getCurrentTrack() {
        try {
            String text = new String(getCurrentTrack.getInputStream().readAllBytes(), Charsets.UTF_8);
            AppleScript as = new AppleScript(text);
            AppleScriptObject result = as.executeAsObject();
            AppleTrack appleTrack = AppleTrackMapper.fromAppleScriptList(result);
            return appleTrack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public AppleEqualizerAndBands getCurrentEq() {
        try {
            String text = new String(getCurrentEq.getInputStream().readAllBytes(), Charsets.UTF_8);
            AppleScript as = new AppleScript(text);
            AppleScriptObject appleScriptObject = as.executeAsObject();
            AtomicReference<String> name = new AtomicReference<>();
            AtomicDouble band1 = new AtomicDouble();
            AtomicDouble band2 = new AtomicDouble();
            AtomicDouble band3 = new AtomicDouble();
            AtomicDouble band4 = new AtomicDouble();
            AtomicDouble band5 = new AtomicDouble();
            AtomicDouble band6 = new AtomicDouble();
            AtomicDouble band7 = new AtomicDouble();
            AtomicDouble band8 = new AtomicDouble();
            AtomicDouble band9 = new AtomicDouble();
            AtomicDouble band10 = new AtomicDouble();
            AtomicDouble preamp = new AtomicDouble();
            appleScriptObject.getList().stream().forEach(object -> {
                String[] split = object.toString().split(":");
                String key = split[0];
                String value = split[1];
                if (value.charAt(0) == '"') {
                    value = value.substring(1);
                }
                if (value.charAt(value.length() - 1)== '"') {
                    value = value.substring(0, value.length() - 1);
                }
                if (key.equals("value")) {
                    name.set(value);
                } else if (key.equals("band1")) {
                    band1.set(getLongValue(value));
                } else if (key.equals("band2")) {
                    band2.set(getLongValue(value));
                } else if (key.equals("band3")) {
                    band3.set(getLongValue(value));
                } else if (key.equals("band4")) {
                    band4.set(getLongValue(value));
                } else if (key.equals("band5")) {
                    band5.set(getLongValue(value));
                } else if (key.equals("band6")) {
                    band6.set(getLongValue(value));
                } else if (key.equals("band7")) {
                    band7.set(getLongValue(value));
                } else if (key.equals("band8")) {
                    band8.set(getLongValue(value));
                } else if (key.equals("band9")) {
                    band9.set(getLongValue(value));
                } else if (key.equals("band10")) {
                    band10.set(getLongValue(value));
                } else if (key.equals("preamp")) {
                    preamp.set(getLongValue(value));
                }
            });
            return new AppleEqualizerAndBands(name.get(), band1.get(), band2.get(), band3.get(), band4.get(), band5.get(),
                    band6.get(), band7.get(), band8.get(), band9.get(), band10.get(), preamp.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private static Double getLongValue(String value) {
        return Double.valueOf(value);
    }

    public AppleTrack getAppleTrackById(int id) {
        try {
            String text = new String(getPropertiesOfTrack.getInputStream().readAllBytes(), Charsets.UTF_8);

            text = text.replace("${track_id}", "" + id);
            AppleScript as = new AppleScript(text);
            AppleScriptObject result = as.executeAsObject();
            return AppleTrackMapper.fromAppleScriptList(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AppleTrack> getAppleTrackByIds(List<Integer> ids) {
        try {
            String str = "id is " + ids.stream()
                    .map(i->i.toString())
                    .collect(Collectors.joining(" or id is "));
            String text = new String(getPropertiesOfTracks.getInputStream().readAllBytes(), Charsets.UTF_8);

            text = text.replace("${track_ids}", str);
            AppleScript as = new AppleScript(text);
            AppleScriptObject result = as.executeAsObject();
            List<AppleTrack> list  = new ArrayList<>();
            result.getList().stream().forEach( a-> list.add(AppleTrackMapper.fromAppleScriptMap(a)));
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AppleEqualizer> getAppleEqualizerList() {
        try {
            String text = new String(getEqualizerList.getInputStream().readAllBytes(), Charsets.UTF_8);

            AppleScript as = new AppleScript(text);
            AppleScriptObject result = as.executeAsObject();
            List<AppleEqualizer> list  = new ArrayList<>();
            AtomicInteger index = new AtomicInteger(1);
            result.getList().stream().forEach( a-> {
                list.add(AppleTrackMapper.fromAppleScriptMapToEq(index.get(), a));
                index.getAndIncrement();
            });
            return list;
        } catch (IOException | AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAllTracksIds() {
        try {
            List<Integer> list = new ArrayList<>();
            String text = new String(getAllTracks.getInputStream().readAllBytes(), Charsets.UTF_8);
            AppleScript as = new AppleScript(text);
            AppleScriptObject result = as.executeAsObject();
            result.getList().forEach( a-> {
                try {
                    list.add(a.getInteger());
                } catch (AppleScriptException e) {
                    throw new RuntimeException(e);
                }
            });
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void search() {
        try {
            String text = new String(search.getInputStream().readAllBytes(), Charsets.UTF_8);
            AppleScript as = new AppleScript(text);
            AppleScriptObject result = as.executeAsObject();
            log.info(result.getString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void emptyTriageList() {
        emptyList(TRIAGE);
    }

    public void emptyList(String playlist) {
        try {
            String text = new String(emptyPlaylist.getInputStream().readAllBytes(), Charsets.UTF_8);
            text = text.replace("${play_list_name}", "" + playlist);
            AppleScript as = new AppleScript(text);
            as.executeAsObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillTriageList(List<Long> ids) {
        fillListBatch(TRIAGE, ids);
    }

    public void fillBestOf(List<Long> ids) {
        fillListBatch(BEST_OF, ids);
    }

    public void fillList(String playlist, List<Long> ids) {
        ids.stream().forEach(id -> {
            boolean inserted = false;
            int retry = 0;
            while (!inserted && retry < 4)
            try {
                String text = new String(addTrackToPlayList.getInputStream().readAllBytes(), Charsets.UTF_8);
                text = text.replace("${play_list_name}", "" + playlist);
                text = text.replace("${track_id}", "" + id);
                AppleScript as = new AppleScript(text);
                as.executeAsObject();
                inserted = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (AppleScriptException e) {
                if (retry == 4) {
                    log.error("can't add id={}", id);
                }
                retry++;
            }
        });
    }

    public void fillListBatch(String playlist, List<Long> ids) {
        String str = ids.stream()
                .map(i->i.toString())
                .collect(Collectors.joining(", "));
        try {
            String text = new String(addTracksToPlayList.getInputStream().readAllBytes(), Charsets.UTF_8);
            text = text.replace("${play_list_name}", "" + playlist);
            text = text.replace("${track_ids}", str);
            AppleScript as = new AppleScript(text);
            as.executeAsObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            log.error("can't add batch",e);
        }
    }

    public void emptyBestOf() {
        emptyList(BEST_OF);
    }
}
