package com.apa.common.entities.media;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlexMediaTest {

    @Test
    public void checkOwnAttributeEquals() {
        PlexMedia plexMedia1 = PlexMedia.builder()
                .plexId("test")
                .build();
        PlexMedia plexMedia2 = PlexMedia.builder()
                .plexId("test")
                .build();
        assertTrue(plexMedia1.equals(plexMedia2));
    }

    @Test
    public void checkOwnAttributeNotEquals() {
        PlexMedia plexMedia1 = PlexMedia.builder()
                .plexId("test")
                .build();
        PlexMedia plexMedia2 = PlexMedia.builder()
                .plexId("test2")
                .build();
        assertFalse(plexMedia1.equals(plexMedia2));
    }

    @Test
    public void checkParentAttributeEquals() {
        PlexMedia plexMedia1 = PlexMedia.builder()
                .artist("test")
                .build();
        PlexMedia plexMedia2 = PlexMedia.builder()
                .artist("test")
                .build();
        assertTrue(plexMedia1.equals(plexMedia2));
    }

    @Test
    public void checkParentAttributeNotEquals() {
        PlexMedia plexMedia1 = PlexMedia.builder()
                .artist("test")
                .build();
        PlexMedia plexMedia2 = PlexMedia.builder()
                .artist("test2")
                .build();
        assertFalse(plexMedia1.equals(plexMedia2));
    }

}