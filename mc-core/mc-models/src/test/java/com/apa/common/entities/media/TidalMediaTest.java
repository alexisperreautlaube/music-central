package com.apa.common.entities.media;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TidalMediaTest {

    @Test
    public void checkOwnAttributeEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .tidalTrackId("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .tidalTrackId("test")
                .build();
        assertTrue(tidalMedia1.equals(tidalMedia2));
    }

    @Test
    public void checkOwnAttributeNotEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .tidalTrackId("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .tidalTrackId("test2")
                .build();
        assertFalse(tidalMedia1.equals(tidalMedia2));
    }

    @Test
    public void checkParentAttributeEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .artistName("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .artistName("test")
                .build();
        assertTrue(tidalMedia1.equals(tidalMedia2));
    }

    @Test
    public void checkParentAttributeNotEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .artistName("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .artistName("test2")
                .build();
        assertFalse(tidalMedia1.equals(tidalMedia2));
    }

}