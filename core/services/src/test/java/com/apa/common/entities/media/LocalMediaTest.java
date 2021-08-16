package com.apa.common.entities.media;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalMediaTest {

    @Test
    public void checkOwnAttributeEquals() {
        LocalMedia localMedia1 = LocalMedia.builder()
                .localId("test")
                .build();
        LocalMedia localMedia2 = LocalMedia.builder()
                .localId("test")
                .build();
        assertTrue(localMedia1.equals(localMedia2));
    }

    @Test
    public void checkOwnAttributeNotEquals() {
        LocalMedia localMedia1 = LocalMedia.builder()
                .localId("test")
                .build();
        LocalMedia localMedia2 = LocalMedia.builder()
                .localId("test2")
                .build();
        assertFalse(localMedia1.equals(localMedia2));
    }

    @Test
    public void checkParentAttributeEquals() {
        LocalMedia localMedia1 = LocalMedia.builder()
                .artist("test")
                .build();
        LocalMedia localMedia2 = LocalMedia.builder()
                .artist("test")
                .build();
        assertTrue(localMedia1.equals(localMedia2));
    }

    @Test
    public void checkParentAttributeNotEquals() {
        LocalMedia localMedia1 = LocalMedia.builder()
                .artist("test")
                .build();
        LocalMedia localMedia2 = LocalMedia.builder()
                .artist("test2")
                .build();
        assertFalse(localMedia1.equals(localMedia2));
    }

}