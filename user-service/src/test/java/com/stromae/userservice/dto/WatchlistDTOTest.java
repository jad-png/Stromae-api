package com.stromae.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistDTOTest {

    @Test
    void builder_CreatesDTO() {
        WatchlistDTO dto = WatchlistDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .addedAt(1700000000000L)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getAddedAt());
    }

    @Test
    void noArgsConstructor() {
        WatchlistDTO dto = new WatchlistDTO();

        assertNull(dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getVideoId());
        assertNull(dto.getAddedAt());
    }

    @Test
    void allArgsConstructor() {
        WatchlistDTO dto = new WatchlistDTO(1L, 10L, 20L, 1700000000000L);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getAddedAt());
    }

    @Test
    void settersAndGetters() {
        WatchlistDTO dto = new WatchlistDTO();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setVideoId(20L);
        dto.setAddedAt(1700000000000L);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getAddedAt());
    }

    @Test
    void equalsAndHashCode() {
        WatchlistDTO d1 = WatchlistDTO.builder().id(1L).userId(10L).videoId(20L).build();
        WatchlistDTO d2 = WatchlistDTO.builder().id(1L).userId(10L).videoId(20L).build();
        WatchlistDTO d3 = WatchlistDTO.builder().id(2L).userId(10L).videoId(30L).build();

        assertEquals(d1, d2);
        assertNotEquals(d1, d3);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void toString_ContainsFields() {
        WatchlistDTO dto = WatchlistDTO.builder().id(1L).userId(10L).videoId(20L).build();
        String str = dto.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
    }
}
