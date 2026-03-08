package com.stromae.userservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistTest {

    @Test
    void builder_CreatesWatchlist() {
        Watchlist watchlist = Watchlist.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .addedAt(1700000000000L)
                .build();

        assertEquals(1L, watchlist.getId());
        assertEquals(10L, watchlist.getUserId());
        assertEquals(20L, watchlist.getVideoId());
        assertEquals(1700000000000L, watchlist.getAddedAt());
    }

    @Test
    void noArgsConstructor_CreatesEmptyWatchlist() {
        Watchlist watchlist = new Watchlist();

        assertNull(watchlist.getId());
        assertNull(watchlist.getUserId());
        assertNull(watchlist.getVideoId());
        assertNull(watchlist.getAddedAt());
    }

    @Test
    void allArgsConstructor_CreatesWatchlist() {
        Watchlist watchlist = new Watchlist(1L, 10L, 20L, 1700000000000L);

        assertEquals(1L, watchlist.getId());
        assertEquals(10L, watchlist.getUserId());
        assertEquals(20L, watchlist.getVideoId());
        assertEquals(1700000000000L, watchlist.getAddedAt());
    }

    @Test
    void settersAndGetters() {
        Watchlist watchlist = new Watchlist();
        watchlist.setId(1L);
        watchlist.setUserId(10L);
        watchlist.setVideoId(20L);
        watchlist.setAddedAt(1700000000000L);

        assertEquals(1L, watchlist.getId());
        assertEquals(10L, watchlist.getUserId());
        assertEquals(20L, watchlist.getVideoId());
        assertEquals(1700000000000L, watchlist.getAddedAt());
    }

    @Test
    void onCreate_SetsAddedAt() {
        Watchlist watchlist = new Watchlist();
        watchlist.onCreate();

        assertNotNull(watchlist.getAddedAt());
        assertTrue(watchlist.getAddedAt() > 0);
    }

    @Test
    void equalsAndHashCode() {
        Watchlist w1 = Watchlist.builder().id(1L).userId(10L).videoId(20L).build();
        Watchlist w2 = Watchlist.builder().id(1L).userId(10L).videoId(20L).build();
        Watchlist w3 = Watchlist.builder().id(2L).userId(10L).videoId(30L).build();

        assertEquals(w1, w2);
        assertNotEquals(w1, w3);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void toString_ContainsFields() {
        Watchlist watchlist = Watchlist.builder().id(1L).userId(10L).videoId(20L).build();
        String str = watchlist.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
    }
}
