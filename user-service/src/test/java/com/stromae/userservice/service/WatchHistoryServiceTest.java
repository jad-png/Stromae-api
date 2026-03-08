package com.stromae.userservice.service;

import com.stromae.userservice.dto.ViewingStatisticsDTO;
import com.stromae.userservice.dto.WatchHistoryDTO;
import com.stromae.userservice.entity.WatchHistory;
import com.stromae.userservice.feign.VideoServiceClient;
import com.stromae.userservice.mapper.WatchHistoryMapper;
import com.stromae.userservice.repository.WatchHistoryRepository;
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
class WatchHistoryServiceTest {

    @Mock
    private WatchHistoryRepository watchHistoryRepository;

    @Mock
    private WatchHistoryMapper watchHistoryMapper;

    @Mock
    private VideoServiceClient videoServiceClient;

    @InjectMocks
    private WatchHistoryService watchHistoryService;

    private WatchHistoryDTO watchHistoryDTO;
    private WatchHistory watchHistory;

    @BeforeEach
    void setUp() {
        watchHistoryDTO = WatchHistoryDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .watchedAt(System.currentTimeMillis())
                .progressTime(120)
                .completed(false)
                .build();

        watchHistory = WatchHistory.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .watchedAt(System.currentTimeMillis())
                .progressTime(120)
                .completed(false)
                .build();
    }

    @Test
    void saveWatchHistory_NewEntry_Success() {
        when(videoServiceClient.getVideoById(20L)).thenReturn(Map.of("id", 20L));
        when(watchHistoryRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.empty());
        when(watchHistoryMapper.toEntity(watchHistoryDTO)).thenReturn(watchHistory);
        when(watchHistoryRepository.save(watchHistory)).thenReturn(watchHistory);
        when(watchHistoryMapper.toDTO(watchHistory)).thenReturn(watchHistoryDTO);

        WatchHistoryDTO result = watchHistoryService.saveWatchHistory(watchHistoryDTO);

        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals(20L, result.getVideoId());
        assertEquals(120, result.getProgressTime());
        assertFalse(result.getCompleted());
        verify(watchHistoryRepository, times(1)).save(watchHistory);
    }

    @Test
    void saveWatchHistory_ExistingEntry_UpdatesProgressAndCompleted() {
        WatchHistory existingHistory = WatchHistory.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .progressTime(60)
                .completed(false)
                .build();

        WatchHistoryDTO updateDTO = WatchHistoryDTO.builder()
                .userId(10L)
                .videoId(20L)
                .progressTime(300)
                .completed(true)
                .build();

        WatchHistoryDTO expectedResult = WatchHistoryDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .progressTime(300)
                .completed(true)
                .build();

        when(videoServiceClient.getVideoById(20L)).thenReturn(Map.of("id", 20L));
        when(watchHistoryRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.of(existingHistory));
        when(watchHistoryRepository.save(existingHistory)).thenReturn(existingHistory);
        when(watchHistoryMapper.toDTO(existingHistory)).thenReturn(expectedResult);

        WatchHistoryDTO result = watchHistoryService.saveWatchHistory(updateDTO);

        assertNotNull(result);
        assertEquals(300, result.getProgressTime());
        assertTrue(result.getCompleted());
        verify(watchHistoryRepository, times(1)).save(existingHistory);
    }

    @Test
    void saveWatchHistory_ExistingEntry_NullProgressTime_DoesNotUpdate() {
        WatchHistory existingHistory = WatchHistory.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .progressTime(60)
                .completed(false)
                .build();

        WatchHistoryDTO updateDTO = WatchHistoryDTO.builder()
                .userId(10L)
                .videoId(20L)
                .progressTime(null)
                .completed(null)
                .build();

        when(videoServiceClient.getVideoById(20L)).thenReturn(Map.of("id", 20L));
        when(watchHistoryRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.of(existingHistory));
        when(watchHistoryRepository.save(existingHistory)).thenReturn(existingHistory);
        when(watchHistoryMapper.toDTO(existingHistory)).thenReturn(WatchHistoryDTO.builder()
                .id(1L).userId(10L).videoId(20L).progressTime(60).completed(false).build());

        WatchHistoryDTO result = watchHistoryService.saveWatchHistory(updateDTO);

        assertNotNull(result);
        assertEquals(60, result.getProgressTime());
        assertFalse(result.getCompleted());
    }

    @Test
    void saveWatchHistory_VideoNotFound_ThrowsException() {
        when(videoServiceClient.getVideoById(20L)).thenThrow(new RuntimeException("Not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> watchHistoryService.saveWatchHistory(watchHistoryDTO));

        assertEquals("Video not found with id: 20", exception.getMessage());
        verify(watchHistoryRepository, never()).save(any());
    }

    @Test
    void getUserWatchHistory_ReturnsList() {
        WatchHistory history2 = WatchHistory.builder().id(2L).userId(10L).videoId(30L).build();
        WatchHistoryDTO dto2 = WatchHistoryDTO.builder().id(2L).userId(10L).videoId(30L).build();

        when(watchHistoryRepository.findByUserId(10L)).thenReturn(Arrays.asList(watchHistory, history2));
        when(watchHistoryMapper.toDTO(watchHistory)).thenReturn(watchHistoryDTO);
        when(watchHistoryMapper.toDTO(history2)).thenReturn(dto2);

        List<WatchHistoryDTO> result = watchHistoryService.getUserWatchHistory(10L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(watchHistoryRepository, times(1)).findByUserId(10L);
    }

    @Test
    void getUserWatchHistory_EmptyList() {
        when(watchHistoryRepository.findByUserId(10L)).thenReturn(Collections.emptyList());

        List<WatchHistoryDTO> result = watchHistoryService.getUserWatchHistory(10L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWatchHistoryByVideoId_Found() {
        when(watchHistoryRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.of(watchHistory));
        when(watchHistoryMapper.toDTO(watchHistory)).thenReturn(watchHistoryDTO);

        WatchHistoryDTO result = watchHistoryService.getWatchHistoryByVideoId(10L, 20L);

        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals(20L, result.getVideoId());
    }

    @Test
    void getWatchHistoryByVideoId_NotFound_ThrowsException() {
        when(watchHistoryRepository.findByUserIdAndVideoId(10L, 20L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> watchHistoryService.getWatchHistoryByVideoId(10L, 20L));

        assertEquals("Watch history not found", exception.getMessage());
    }

    @Test
    void getViewingStatistics_WithData() {
        when(watchHistoryRepository.countDistinctVideosByUserId(10L)).thenReturn(5L);
        when(watchHistoryRepository.countCompletedVideosByUserId(10L)).thenReturn(3L);
        when(watchHistoryRepository.sumProgressTimeByUserId(10L)).thenReturn(1500L);

        ViewingStatisticsDTO result = watchHistoryService.getViewingStatistics(10L);

        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals(5L, result.getTotalVideosWatched());
        assertEquals(3L, result.getTotalCompletedVideos());
        assertEquals(1500L, result.getTotalTimeWatched());
        assertEquals(60.0, result.getAverageCompletionRate());
    }

    @Test
    void getViewingStatistics_NoData() {
        when(watchHistoryRepository.countDistinctVideosByUserId(10L)).thenReturn(0L);
        when(watchHistoryRepository.countCompletedVideosByUserId(10L)).thenReturn(0L);
        when(watchHistoryRepository.sumProgressTimeByUserId(10L)).thenReturn(null);

        ViewingStatisticsDTO result = watchHistoryService.getViewingStatistics(10L);

        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals(0L, result.getTotalVideosWatched());
        assertEquals(0L, result.getTotalCompletedVideos());
        assertEquals(0L, result.getTotalTimeWatched());
        assertEquals(0.0, result.getAverageCompletionRate());
    }

    @Test
    void getViewingStatistics_NullCounts() {
        when(watchHistoryRepository.countDistinctVideosByUserId(10L)).thenReturn(null);
        when(watchHistoryRepository.countCompletedVideosByUserId(10L)).thenReturn(null);
        when(watchHistoryRepository.sumProgressTimeByUserId(10L)).thenReturn(null);

        ViewingStatisticsDTO result = watchHistoryService.getViewingStatistics(10L);

        assertNotNull(result);
        assertEquals(0L, result.getTotalVideosWatched());
        assertEquals(0L, result.getTotalCompletedVideos());
        assertEquals(0L, result.getTotalTimeWatched());
    }
}
