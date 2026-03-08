package com.stromae.userservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchHistoryTest {

    @Test
    void builder_CreatesWatchHistory() {
        WatchHistory history = WatchHistory.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .watchedAt(1700000000000L)
                .progressTime(120)
                .completed(false)
                .build();

        assertEquals(1L, history.getId());
        assertEquals(10L, history.getUserId());
        assertEquals(20L, history.getVideoId());
        assertEquals(1700000000000L, history.getWatchedAt());
        assertEquals(120, history.getProgressTime());
        assertFalse(history.getCompleted());
    }

    @Test
    void noArgsConstructor_CreatesEmptyWatchHistory() {
        WatchHistory history = new WatchHistory();

        assertNull(history.getId());
        assertNull(history.getUserId());
        assertNull(history.getVideoId());
        assertNull(history.getWatchedAt());
        assertNull(history.getProgressTime());
        assertNull(history.getCompleted());
    }

    @Test
    void allArgsConstructor_CreatesWatchHistory() {
        WatchHistory history = new WatchHistory(1L, 10L, 20L, 1700000000000L, 120, false);

        assertEquals(1L, history.getId());
        assertEquals(10L, history.getUserId());
        assertEquals(20L, history.getVideoId());
        assertEquals(1700000000000L, history.getWatchedAt());
        assertEquals(120, history.getProgressTime());
        assertFalse(history.getCompleted());
    }

    @Test
    void settersAndGetters() {
        WatchHistory history = new WatchHistory();
        history.setId(1L);
        history.setUserId(10L);
        history.setVideoId(20L);
        history.setWatchedAt(1700000000000L);
        history.setProgressTime(300);
        history.setCompleted(true);

        assertEquals(1L, history.getId());
        assertEquals(10L, history.getUserId());
        assertEquals(20L, history.getVideoId());
        assertEquals(1700000000000L, history.getWatchedAt());
        assertEquals(300, history.getProgressTime());
        assertTrue(history.getCompleted());
    }

    @Test
    void onCreate_SetsWatchedAtAndDefaultCompleted() {
        WatchHistory history = new WatchHistory();
        history.onCreate();

        assertNotNull(history.getWatchedAt());
        assertTrue(history.getWatchedAt() > 0);
        assertFalse(history.getCompleted());
    }

    @Test
    void onCreate_DoesNotOverrideCompletedIfAlreadySet() {
        WatchHistory history = new WatchHistory();
        history.setCompleted(true);
        history.onCreate();

        assertTrue(history.getCompleted());
        assertNotNull(history.getWatchedAt());
    }

    @Test
    void equalsAndHashCode() {
        WatchHistory h1 = WatchHistory.builder().id(1L).userId(10L).videoId(20L).progressTime(100).completed(false).build();
        WatchHistory h2 = WatchHistory.builder().id(1L).userId(10L).videoId(20L).progressTime(100).completed(false).build();
        WatchHistory h3 = WatchHistory.builder().id(2L).userId(10L).videoId(30L).progressTime(200).completed(true).build();

        assertEquals(h1, h2);
        assertNotEquals(h1, h3);
        assertEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void toString_ContainsFields() {
        WatchHistory history = WatchHistory.builder().id(1L).userId(10L).videoId(20L).progressTime(120).completed(false).build();
        String str = history.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
        assertTrue(str.contains("120"));
    }
}
