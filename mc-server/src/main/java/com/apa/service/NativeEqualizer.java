package com.apa.service;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


import com.sun.jna.Native;
import lombok.Getter;

public class NativeEqualizer {

    @Getter
    public enum Presets {
        ACOUSTIC(new float[] {0.0f , 5.0f , 4.900000095367f , 3.950000047684f , 1.049999952316f , 2.150000095367f , 1.75f , 3.5f , 4.099999904633f , 3.549999952316f , 2.150000095367f}),
        CLASSICAL(new float[] {0.0f , 4.75f , 3.75f , 3.0f , 2.5f , -1.5f , -1.5f , 0.0f , 2.25f , 3.25f , 3.75f}),
        DANCE(new float[] {0.0f , 3.569999933243f , 6.550000190735f , 4.989999771118f , 0.0f , 1.919999957085f , 3.650000095367f , 5.150000095367f , 4.539999961853f , 3.589999914169f , 0.0f}),
        DEEP(new float[] {0.0f , 4.949999809265f , 3.549999952316f , 1.75f , 1.0f , 2.849999904633f , 2.5f , 1.450000047684f , -2.150000095367f , -3.549999952316f , -4.599999904633f}),
        ELECTRONIC(new float[] {0.0f , 4.25f , 3.799999952316f , 1.200000047684f , 0.0f , -2.150000095367f , 2.25f , 0.850000023842f , 1.25f , 3.950000047684f , 4.800000190735f}),
        FLAT(new float[] {0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f}),
        HIP_HOP(new float[] {0.0f , 5.0f , 4.25f , 1.5f , 3.0f , -1.0f , -1.0f , 1.5f , -0.5f , 2.0f , 3.0f}),
        INCREASE_BASS(new float[] {0.0f , 5.5f , 4.25f , 3.5f , 2.5f , 1.25f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f}),
        INCREASE_TREBLE(new float[] {0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 1.25f , 2.5f , 3.5f , 4.25f , 5.5f}),
        INCREASE_VOCALS(new float[] {0.0f , -1.5f , -3.0f , -3.0f , 1.5f , 3.75f , 3.75f , 3.0f , 1.5f , 0.0f , -1.5f}),
        JAZZ(new float[] {0.0f , 4.0f , 3.0f , 1.5f , 2.25f , -1.5f , -1.5f , 0.0f , 1.5f , 3.0f , 3.75f}),
        LATIN(new float[] {0.0f , 4.5f , 3.0f , 0.0f , 0.0f , -1.5f , -1.5f , -1.5f , 0.0f , 3.0f , 4.5f}),
        LOUDNESS(new float[] {0.0f , 6.0f , 4.0f , 0.0f , 0.0f , -2.0f , 0.0f , -1.0f , -5.0f , 5.0f , 1.0f}),
        LOUNGE(new float[] {0.0f , -3.0f , -1.5f , -0.5f , 1.5f , 4.0f , 2.5f , 0.0f , -1.5f , 2.0f , 1.0f}),
        PIANO(new float[] {0.0f , 3.0f , 2.0f , 0.0f , 2.5f , 3.0f , 1.5f , 3.5f , 4.5f , 3.0f , 3.5f}),
        POP(new float[] {0.0f , -1.5f , -1.0f , 0.0f , 2.0f , 4.0f , 4.0f , 2.0f , 0.0f , -1.0f , -1.5f}),
        R_AND_B(new float[] {0.0f , 2.619999885559f , 6.920000076294f , 5.650000095367f , 1.330000042915f , -2.19000005722f , -1.5f , 2.319999933243f , 2.650000095367f , 3.0f , 3.75f}),
        REDUCE_BASS(new float[] {0.0f , -5.5f , -4.25f , -3.5f , -2.5f , -1.25f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f}),
        REDUCE_TREBLE(new float[] {0.0f , 0.0f , 0.0f , 0.0f , 0.0f , 0.0f , -1.25f , -2.5f , -3.5f , -4.25f , -5.5f}),
        ROCK(new float[] {0.0f , 5.0f , 4.0f , 3.0f , 1.5f , -0.5f , -1.0f , 0.5f , 2.5f , 3.5f , 4.5f}),
        SMALL_SPEAKERS(new float[] {0.0f , 5.5f , 4.25f , 3.5f , 2.5f , 1.25f , 0.0f , -1.25f , -2.5f , -3.5f , -4.25f}),
        SPOKEN_WORD(new float[] {0.0f , -3.460000038147f , -0.469999998808f , 0.0f , 0.689999997616f , 3.460000038147f , 4.610000133514f , 4.840000152588f , 4.280000209808f , 2.539999961853f , 0.0f});


        private final float[] bands;

        Presets(float[] floats) {
            bands = floats;
        }
    }

    // Define the Core Audio library
    public interface CoreAudio extends Library {
        CoreAudio INSTANCE = Native.load("CoreAudio", CoreAudio.class);

        // Functions for managing audio devices
        int AudioObjectGetPropertyData(int inObjectID, PointerByReference inAddress, int inQualifierDataSize,
                                       Pointer inQualifierData, IntByReference ioDataSize, Pointer outData);

        int AudioObjectSetPropertyData(int inObjectID, PointerByReference inAddress, int inQualifierDataSize,
                                       Pointer inQualifierData, int inDataSize, Pointer inData);
    }

    public void setEqualizer(String name) {
        Presets preset = Presets.valueOf(name);
        setEqualizer(preset.getBands());
    }

    public  void setEqualizer(float[] eqSettings) { // = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }; // Set the equalizer settings here
        // Define the ID of the audio device to adjust the equalizer
        int deviceID = getDeviceID();

        // Define the address for the equalizer property
        PointerByReference eqAddress = new PointerByReference();
        eqAddress.setValue(new Memory(Native.POINTER_SIZE));
        eqAddress.getPointer().setInt(0, 2); // Set the equalizer property ID

        // Set the equalizer settings for the device
        CoreAudio.INSTANCE.AudioObjectSetPropertyData(deviceID, eqAddress, 0, null,
                eqSettings.length * Native.getNativeSize(Float.TYPE), new Memory(eqSettings.length * Native.getNativeSize(Float.TYPE)));
    }

    // Helper function to get the ID of the audio device
    private int getDeviceID() {
        PointerByReference defaultDevice = new PointerByReference();
        CoreAudio.INSTANCE.AudioObjectGetPropertyData(-1, defaultDevice, 0, null, new IntByReference(Native.POINTER_SIZE),
                null);
        return defaultDevice.getPointer().getInt(0);
    }
}
