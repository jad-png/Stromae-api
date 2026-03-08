package com.stromae.userservice.service;

import com.stromae.userservice.dto.WatchlistDTO;
import com.stromae.userservice.entity.Watchlist;
import com.stromae.userservice.feign.VideoServiceClient;
import com.stromae.userservice.mapper.WatchlistMapper;
import com.stromae.userservice.repository.WatchlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceTest {

    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private WatchlistMapper watchlistMapper;

    @Mock
    private VideoServiceClient videoServiceClient;

    @InjectMocks
    private WatchlistService watchlistService;

    private WatchlistDTO watchlistDTO;
    private Watchlist watchlist;

    @BeforeEach
    void setUp() {
        watchlistDTO = WatchlistDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .addedAt(System.currentTimeMillis())
                .build();

        watchlist = Watchlist.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .addedAt(System.currentTimeMillis())
                .build();
    }

    @Test
    void addToWatchlist_Success() {
        when(videoServiceClient.getVideoById(20L)).thenReturn(Map.of("id", 20L));
        when(watchlistRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.empty());
        when(watchlistMapper.toEntity(watchlistDTO)).thenReturn(watchlist);
        when(watchlistRepository.save(watchlist)).thenReturn(watchlist);
        when(watchlistMapper.toDTO(watchlist)).thenReturn(watchlistDTO);

        WatchlistDTO result = watchlistService.addToWatchlist(watchlistDTO);

        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals(20L, result.getVideoId());
        verify(videoServiceClient, times(1)).getVideoById(20L);
        verify(watchlistRepository, times(1)).save(watchlist);
    }

    @Test
    void addToWatchlist_VideoNotFound_ThrowsException() {
        when(videoServiceClient.getVideoById(20L)).thenThrow(new RuntimeException("Not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> watchlistService.addToWatchlist(watchlistDTO));

        assertEquals("Video not found with id: 20", exception.getMessage());
        verify(watchlistRepository, never()).save(any());
    }

    @Test
    void addToWatchlist_AlreadyExists_ThrowsException() {
        when(videoServiceClient.getVideoById(20L)).thenReturn(Map.of("id", 20L));
        when(watchlistRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.of(watchlist));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> watchlistService.addToWatchlist(watchlistDTO));

        assertEquals("Video already in watchlist", exception.getMessage());
        verify(watchlistRepository, never()).save(any());
    }

    @Test
    void getUserWatchlist_ReturnsListOfItems() {
        Watchlist watchlist2 = Watchlist.builder().id(2L).userId(10L).videoId(30L).build();
        WatchlistDTO dto2 = WatchlistDTO.builder().id(2L).userId(10L).videoId(30L).build();

        when(watchlistRepository.findByUserId(10L)).thenReturn(Arrays.asList(watchlist, watchlist2));
        when(watchlistMapper.toDTO(watchlist)).thenReturn(watchlistDTO);
        when(watchlistMapper.toDTO(watchlist2)).thenReturn(dto2);

        List<WatchlistDTO> result = watchlistService.getUserWatchlist(10L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(watchlistRepository, times(1)).findByUserId(10L);
    }

    @Test
    void getUserWatchlist_EmptyList() {
        when(watchlistRepository.findByUserId(10L)).thenReturn(Collections.emptyList());

        List<WatchlistDTO> result = watchlistService.getUserWatchlist(10L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void removeFromWatchlist_Success() {
        doNothing().when(watchlistRepository).deleteByUserIdAndVideoId(10L, 20L);

        watchlistService.removeFromWatchlist(10L, 20L);

        verify(watchlistRepository, times(1)).deleteByUserIdAndVideoId(10L, 20L);
    }

    @Test
    void isVideoInWatchlist_ReturnsTrue() {
        when(watchlistRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.of(watchlist));

        boolean result = watchlistService.isVideoInWatchlist(10L, 20L);

        assertTrue(result);
        verify(watchlistRepository, times(1)).findByUserIdAndVideoId(10L, 20L);
    }

    @Test
    void isVideoInWatchlist_ReturnsFalse() {
        when(watchlistRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.empty());

        boolean result = watchlistService.isVideoInWatchlist(10L, 20L);

        assertFalse(result);
    }
}
