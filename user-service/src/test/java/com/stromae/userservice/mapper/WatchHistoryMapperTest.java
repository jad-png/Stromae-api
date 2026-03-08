package com.stromae.userservice.mapper;

import com.stromae.userservice.dto.WatchHistoryDTO;
import com.stromae.userservice.entity.WatchHistory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class WatchHistoryMapperTest {

    private final WatchHistoryMapper watchHistoryMapper = Mappers.getMapper(WatchHistoryMapper.class);

    @Test
    void toDTO_shouldMapAllFields() {
        WatchHistory watchHistory = WatchHistory.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .watchedAt(1700000000000L)
                .progressTime(3600)
                .completed(true)
                .build();

        WatchHistoryDTO dto = watchHistoryMapper.toDTO(watchHistory);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getWatchedAt());
        assertEquals(3600, dto.getProgressTime());
        assertTrue(dto.getCompleted());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        WatchHistoryDTO dto = WatchHistoryDTO.builder()
                .id(2L)
                .userId(11L)
                .videoId(21L)
                .watchedAt(1700000000000L)
                .progressTime(1800)
                .completed(false)
                .build();

        WatchHistory watchHistory = watchHistoryMapper.toEntity(dto);

        assertEquals(2L, watchHistory.getId());
        assertEquals(11L, watchHistory.getUserId());
        assertEquals(21L, watchHistory.getVideoId());
        assertEquals(1700000000000L, watchHistory.getWatchedAt());
        assertEquals(1800, watchHistory.getProgressTime());
        assertFalse(watchHistory.getCompleted());
    }

    @Test
    void toDTO_withNullEntity_shouldReturnNull() {
        assertNull(watchHistoryMapper.toDTO(null));
    }

    @Test
    void toEntity_withNullDTO_shouldReturnNull() {
        assertNull(watchHistoryMapper.toEntity(null));
    }

    @Test
    void toDTO_withNullFields_shouldMapNulls() {
        WatchHistory watchHistory = WatchHistory.builder().id(1L).build();

        WatchHistoryDTO dto = watchHistoryMapper.toDTO(watchHistory);

        assertEquals(1L, dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getVideoId());
        assertNull(dto.getWatchedAt());
        assertNull(dto.getProgressTime());
        assertNull(dto.getCompleted());
    }

    @Test
    void roundTrip_shouldPreserveData() {
        WatchHistoryDTO original = WatchHistoryDTO.builder()
                .id(5L)
                .userId(100L)
                .videoId(200L)
                .watchedAt(1700000000000L)
                .progressTime(7200)
                .completed(true)
                .build();

        WatchHistory entity = watchHistoryMapper.toEntity(original);
        WatchHistoryDTO result = watchHistoryMapper.toDTO(entity);

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getUserId(), result.getUserId());
        assertEquals(original.getVideoId(), result.getVideoId());
        assertEquals(original.getWatchedAt(), result.getWatchedAt());
        assertEquals(original.getProgressTime(), result.getProgressTime());
        assertEquals(original.getCompleted(), result.getCompleted());
    }

}
