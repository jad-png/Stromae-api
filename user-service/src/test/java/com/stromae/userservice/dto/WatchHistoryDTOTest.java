package com.stromae.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchHistoryDTOTest {

    @Test
    void builder_CreatesDTO() {
        WatchHistoryDTO dto = WatchHistoryDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .watchedAt(1700000000000L)
                .progressTime(120)
                .completed(false)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getWatchedAt());
        assertEquals(120, dto.getProgressTime());
        assertFalse(dto.getCompleted());
    }

    @Test
    void noArgsConstructor() {
        WatchHistoryDTO dto = new WatchHistoryDTO();

        assertNull(dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getVideoId());
        assertNull(dto.getWatchedAt());
        assertNull(dto.getProgressTime());
        assertNull(dto.getCompleted());
    }

    @Test
    void allArgsConstructor() {
        WatchHistoryDTO dto = new WatchHistoryDTO(1L, 10L, 20L, 1700000000000L, 120, false);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getWatchedAt());
        assertEquals(120, dto.getProgressTime());
        assertFalse(dto.getCompleted());
    }

    @Test
    void settersAndGetters() {
        WatchHistoryDTO dto = new WatchHistoryDTO();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setVideoId(20L);
        dto.setWatchedAt(1700000000000L);
        dto.setProgressTime(300);
        dto.setCompleted(true);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getWatchedAt());
        assertEquals(300, dto.getProgressTime());
        assertTrue(dto.getCompleted());
    }

    @Test
    void equalsAndHashCode() {
        WatchHistoryDTO d1 = WatchHistoryDTO.builder().id(1L).userId(10L).videoId(20L).progressTime(100).completed(false).build();
        WatchHistoryDTO d2 = WatchHistoryDTO.builder().id(1L).userId(10L).videoId(20L).progressTime(100).completed(false).build();
        WatchHistoryDTO d3 = WatchHistoryDTO.builder().id(2L).userId(10L).videoId(30L).progressTime(200).completed(true).build();

        assertEquals(d1, d2);
        assertNotEquals(d1, d3);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void toString_ContainsFields() {
        WatchHistoryDTO dto = WatchHistoryDTO.builder().id(1L).userId(10L).videoId(20L).progressTime(120).completed(false).build();
        String str = dto.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
        assertTrue(str.contains("120"));
    }
}
