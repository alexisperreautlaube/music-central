package com.apa.common.entities.media;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TidalMediaTest {

    @Test
    public void checkOwnAttributeEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .tidalId("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .tidalId("test")
                .build();
        assertTrue(tidalMedia1.equals(tidalMedia2));
    }

    @Test
    public void checkOwnAttributeNotEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .tidalId("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .tidalId("test2")
                .build();
        assertFalse(tidalMedia1.equals(tidalMedia2));
    }

    @Test
    public void checkParentAttributeEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .artist("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .artist("test")
                .build();
        assertTrue(tidalMedia1.equals(tidalMedia2));
    }

    @Test
    public void checkParentAttributeNotEquals() {
        TidalMedia tidalMedia1 = TidalMedia.builder()
                .artist("test")
                .build();
        TidalMedia tidalMedia2 = TidalMedia.builder()
                .artist("test2")
                .build();
        assertFalse(tidalMedia1.equals(tidalMedia2));
    }

}