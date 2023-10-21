package com.apa.service;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;

import org.springframework.stereotype.Component;

@Component
public class NativeClient {
    public interface AppleMusicLibrary extends Library {
        AppleMusicLibrary INSTANCE = Native.load((Platform.isMac() ? "Music" : "Music.app"), AppleMusicLibrary.class);

        int MusicApplicationGetCurrentTrackPersistentID(Pointer outPID);
        void MusicApplicationSetEQPreset(Pointer presetName);
    }

    public void setEqualizerPreset(String presetName) {
        Pointer presetNamePtr = new Memory(presetName.length() + 1);
        presetNamePtr.setString(0, presetName);

        AppleMusicLibrary.INSTANCE.MusicApplicationSetEQPreset(presetNamePtr);
    }
}
