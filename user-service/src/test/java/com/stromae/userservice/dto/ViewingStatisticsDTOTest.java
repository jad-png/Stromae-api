package com.stromae.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewingStatisticsDTOTest {

    @Test
    void builder_CreatesDTO() {
        ViewingStatisticsDTO dto = ViewingStatisticsDTO.builder()
                .userId(10L)
                .totalVideosWatched(5L)
                .totalCompletedVideos(3L)
                .totalTimeWatched(1500L)
                .averageCompletionRate(60.0)
                .build();

        assertEquals(10L, dto.getUserId());
        assertEquals(5L, dto.getTotalVideosWatched());
        assertEquals(3L, dto.getTotalCompletedVideos());
        assertEquals(1500L, dto.getTotalTimeWatched());
        assertEquals(60.0, dto.getAverageCompletionRate());
    }

    @Test
    void noArgsConstructor() {
        ViewingStatisticsDTO dto = new ViewingStatisticsDTO();

        assertNull(dto.getUserId());
        assertNull(dto.getTotalVideosWatched());
        assertNull(dto.getTotalCompletedVideos());
        assertNull(dto.getTotalTimeWatched());
        assertNull(dto.getAverageCompletionRate());
    }

    @Test
    void allArgsConstructor() {
        ViewingStatisticsDTO dto = new ViewingStatisticsDTO(10L, 5L, 3L, 1500L, 60.0);

        assertEquals(10L, dto.getUserId());
        assertEquals(5L, dto.getTotalVideosWatched());
        assertEquals(3L, dto.getTotalCompletedVideos());
        assertEquals(1500L, dto.getTotalTimeWatched());
        assertEquals(60.0, dto.getAverageCompletionRate());
    }

    @Test
    void settersAndGetters() {
        ViewingStatisticsDTO dto = new ViewingStatisticsDTO();
        dto.setUserId(10L);
        dto.setTotalVideosWatched(5L);
        dto.setTotalCompletedVideos(3L);
        dto.setTotalTimeWatched(1500L);
        dto.setAverageCompletionRate(60.0);

        assertEquals(10L, dto.getUserId());
        assertEquals(5L, dto.getTotalVideosWatched());
        assertEquals(3L, dto.getTotalCompletedVideos());
        assertEquals(1500L, dto.getTotalTimeWatched());
        assertEquals(60.0, dto.getAverageCompletionRate());
    }

    @Test
    void equalsAndHashCode() {
        ViewingStatisticsDTO d1 = ViewingStatisticsDTO.builder().userId(10L).totalVideosWatched(5L).build();
        ViewingStatisticsDTO d2 = ViewingStatisticsDTO.builder().userId(10L).totalVideosWatched(5L).build();
        ViewingStatisticsDTO d3 = ViewingStatisticsDTO.builder().userId(20L).totalVideosWatched(10L).build();

        assertEquals(d1, d2);
        assertNotEquals(d1, d3);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void toString_ContainsFields() {
        ViewingStatisticsDTO dto = ViewingStatisticsDTO.builder()
                .userId(10L).totalVideosWatched(5L).totalCompletedVideos(3L)
                .totalTimeWatched(1500L).averageCompletionRate(60.0).build();
        String str = dto.toString();

        assertTrue(str.contains("10"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("1500"));
        assertTrue(str.contains("60.0"));
    }
}
