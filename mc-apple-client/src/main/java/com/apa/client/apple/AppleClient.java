package com.apa.client.apple;

import com.apa.client.apple.entity.AppleTrack;
import com.apa.client.apple.mapper.AppleTrackMapper;
import com.github.pireba.applescript.AppleScript;
import com.github.pireba.applescript.AppleScriptException;
import com.github.pireba.applescript.AppleScriptObject;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public void playPause() {
        try {
            AppleScript as = new AppleScript(playPauseScript.getFile());
            String result = as.executeAsString();
            log.info(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void getCurrentTrack() {
        try {
            AppleScript as = new AppleScript(getCurrentTrack.getFile());
            AppleScriptObject result = as.executeAsObject();
            AppleTrack appleTrack = AppleTrackMapper.fromAppleScriptList(result);
            log.info("" + appleTrack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AppleScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public AppleTrack getAppleTrackById(int id) {
        try {
            File file = getPropertiesOfTrack.getFile();
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            String joined = Joiner.on(" ").join(lines);
            joined = joined.replace("${track_id}", "" + id);
            AppleScript as = new AppleScript(joined);
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

            File file = getPropertiesOfTracks.getFile();
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            String joined = Joiner.on(" ").join(lines);
            joined = joined.replace("${track_ids}", str);
            AppleScript as = new AppleScript(joined);
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

    public List<Integer> getAllTracksIds() {
        try {
            List<Integer> list = new ArrayList<>();
            AppleScript as = new AppleScript(getAllTracks.getFile());
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
            AppleScript as = new AppleScript(search.getFile());
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
            File file = emptyPlaylist.getFile();
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            String joined = Joiner.on(" ").join(lines);
            joined = joined.replace("${play_list_name}", "" + playlist);
            AppleScript as = new AppleScript(joined);
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
                File file = addTrackToPlayList.getFile();
                List<String> lines = Files.readLines(file, Charsets.UTF_8);
                String joined = Joiner.on(" ").join(lines);
                joined = joined.replace("${play_list_name}", "" + playlist);
                joined = joined.replace("${track_id}", "" + id);
                AppleScript as = new AppleScript(joined);
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
        String str = "id is " + ids.stream()
                .map(i->i.toString())
                .collect(Collectors.joining(" or id is "));
        try {
            File file = addTracksToPlayList.getFile();
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            String joined = Joiner.on(" ").join(lines);
            joined = joined.replace("${play_list_name}", "" + playlist);
            joined = joined.replace("${track_ids}", str);
            AppleScript as = new AppleScript(joined);
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
