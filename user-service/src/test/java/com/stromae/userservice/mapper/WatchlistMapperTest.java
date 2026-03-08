package com.stromae.userservice.mapper;

import com.stromae.userservice.dto.WatchlistDTO;
import com.stromae.userservice.entity.Watchlist;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistMapperTest {

    private final WatchlistMapper watchlistMapper = Mappers.getMapper(WatchlistMapper.class);

    @Test
    void toDTO_shouldMapAllFields() {
        Watchlist watchlist = Watchlist.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .addedAt(1700000000000L)
                .build();

        WatchlistDTO dto = watchlistMapper.toDTO(watchlist);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getVideoId());
        assertEquals(1700000000000L, dto.getAddedAt());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        WatchlistDTO dto = WatchlistDTO.builder()
                .id(2L)
                .userId(11L)
                .videoId(21L)
                .addedAt(1700000000000L)
                .build();

        Watchlist watchlist = watchlistMapper.toEntity(dto);

        assertEquals(2L, watchlist.getId());
        assertEquals(11L, watchlist.getUserId());
        assertEquals(21L, watchlist.getVideoId());
        assertEquals(1700000000000L, watchlist.getAddedAt());
    }

    @Test
    void toDTO_withNullEntity_shouldReturnNull() {
        assertNull(watchlistMapper.toDTO(null));
    }

    @Test
    void toEntity_withNullDTO_shouldReturnNull() {
        assertNull(watchlistMapper.toEntity(null));
    }

    @Test
    void toDTO_withNullFields_shouldMapNulls() {
        Watchlist watchlist = Watchlist.builder().id(1L).build();

        WatchlistDTO dto = watchlistMapper.toDTO(watchlist);

        assertEquals(1L, dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getVideoId());
        assertNull(dto.getAddedAt());
    }

    @Test
    void roundTrip_shouldPreserveData() {
        WatchlistDTO original = WatchlistDTO.builder()
                .id(3L)
                .userId(100L)
                .videoId(200L)
                .addedAt(1700000000000L)
                .build();

        Watchlist entity = watchlistMapper.toEntity(original);
        WatchlistDTO result = watchlistMapper.toDTO(entity);

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getUserId(), result.getUserId());
        assertEquals(original.getVideoId(), result.getVideoId());
        assertEquals(original.getAddedAt(), result.getAddedAt());
    }

}
