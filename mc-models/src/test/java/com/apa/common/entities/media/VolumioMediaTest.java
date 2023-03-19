package com.apa.common.entities.media;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VolumioMediaTest {

    @Test
    public void checkOwnAttributeEquals() {
        VolumioMedia volumioMedia1 = VolumioMedia.builder()
                .trackUri("test")
                .build();
        VolumioMedia volumioMedia2 = VolumioMedia.builder()
                .trackUri("test")
                .build();
        assertTrue(volumioMedia1.equals(volumioMedia2));
    }

    @Test
    public void checkOwnAttributeNotEquals() {
        VolumioMedia volumioMedia1 = VolumioMedia.builder()
                .trackUri("test")
                .build();
        VolumioMedia volumioMedia2 = VolumioMedia.builder()
                .trackUri("test2")
                .build();
        assertFalse(volumioMedia1.equals(volumioMedia2));
    }

    @Test
    public void checkParentAttributeEquals() {
        VolumioMedia volumioMedia1 = VolumioMedia.builder()
                .trackArtist("test")
                .build();
        VolumioMedia volumioMedia2 = VolumioMedia.builder()
                .trackArtist("test")
                .build();
        assertTrue(volumioMedia1.equals(volumioMedia2));
    }

    @Test
    public void checkParentAttributeNotEquals() {
        VolumioMedia volumioMedia1 = VolumioMedia.builder()
                .trackArtist("test")
                .build();
        VolumioMedia volumioMedia2 = VolumioMedia.builder()
                .trackArtist("test2")
                .build();
        assertFalse(volumioMedia1.equals(volumioMedia2));
    }

    @Test
    public void differentUUIDEquals() {
        VolumioMedia volumioMedia1 = VolumioMedia.builder()
                .trackArtist("test")
                .build();
        VolumioMedia volumioMedia2 = VolumioMedia.builder()
                .trackArtist("test")
                .build();
        assertTrue(volumioMedia1.equals(volumioMedia2));
    }

}