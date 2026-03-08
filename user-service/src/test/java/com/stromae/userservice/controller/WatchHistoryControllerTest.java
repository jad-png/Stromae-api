package com.stromae.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stromae.userservice.dto.ViewingStatisticsDTO;
import com.stromae.userservice.dto.WatchHistoryDTO;
import com.stromae.userservice.service.WatchHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WatchHistoryController.class)
class WatchHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WatchHistoryService watchHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private WatchHistoryDTO watchHistoryDTO;

    @BeforeEach
    void setUp() {
        watchHistoryDTO = WatchHistoryDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .watchedAt(1700000000000L)
                .progressTime(120)
                .completed(false)
                .build();
    }

    @Test
    void saveWatchHistory_ReturnsCreated() throws Exception {
        when(watchHistoryService.saveWatchHistory(any(WatchHistoryDTO.class))).thenReturn(watchHistoryDTO);

        mockMvc.perform(post("/api/watch-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(watchHistoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.videoId").value(20L))
                .andExpect(jsonPath("$.progressTime").value(120))
                .andExpect(jsonPath("$.completed").value(false));

        verify(watchHistoryService, times(1)).saveWatchHistory(any(WatchHistoryDTO.class));
    }

    @Test
    void saveWatchHistory_VideoNotFound_ReturnsServerError() throws Exception {
        when(watchHistoryService.saveWatchHistory(any(WatchHistoryDTO.class)))
                .thenThrow(new RuntimeException("Video not found with id: 20"));

        mockMvc.perform(post("/api/watch-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(watchHistoryDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getUserWatchHistory_ReturnsList() throws Exception {
        WatchHistoryDTO dto2 = WatchHistoryDTO.builder()
                .id(2L).userId(10L).videoId(30L).watchedAt(1700000001000L)
                .progressTime(240).completed(true).build();
        List<WatchHistoryDTO> history = Arrays.asList(watchHistoryDTO, dto2);

        when(watchHistoryService.getUserWatchHistory(10L)).thenReturn(history);

        mockMvc.perform(get("/api/watch-history/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].videoId").value(20L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].videoId").value(30L))
                .andExpect(jsonPath("$[1].completed").value(true));

        verify(watchHistoryService, times(1)).getUserWatchHistory(10L);
    }

    @Test
    void getUserWatchHistory_EmptyList() throws Exception {
        when(watchHistoryService.getUserWatchHistory(10L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/watch-history/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getWatchHistoryByVideoId_Found() throws Exception {
        when(watchHistoryService.getWatchHistoryByVideoId(10L, 20L)).thenReturn(watchHistoryDTO);

        mockMvc.perform(get("/api/watch-history/user/10/video/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.videoId").value(20L))
                .andExpect(jsonPath("$.progressTime").value(120));

        verify(watchHistoryService, times(1)).getWatchHistoryByVideoId(10L, 20L);
    }

    @Test
    void getWatchHistoryByVideoId_NotFound_ReturnsServerError() throws Exception {
        when(watchHistoryService.getWatchHistoryByVideoId(10L, 20L))
                .thenThrow(new RuntimeException("Watch history not found"));

        mockMvc.perform(get("/api/watch-history/user/10/video/20"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getViewingStatistics_ReturnsStatistics() throws Exception {
        ViewingStatisticsDTO statistics = ViewingStatisticsDTO.builder()
                .userId(10L)
                .totalVideosWatched(5L)
                .totalCompletedVideos(3L)
                .totalTimeWatched(1500L)
                .averageCompletionRate(60.0)
                .build();

        when(watchHistoryService.getViewingStatistics(10L)).thenReturn(statistics);

        mockMvc.perform(get("/api/watch-history/statistics/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.totalVideosWatched").value(5L))
                .andExpect(jsonPath("$.totalCompletedVideos").value(3L))
                .andExpect(jsonPath("$.totalTimeWatched").value(1500L))
                .andExpect(jsonPath("$.averageCompletionRate").value(60.0));

        verify(watchHistoryService, times(1)).getViewingStatistics(10L);
    }

    @Test
    void getViewingStatistics_NoData_ReturnsZeros() throws Exception {
        ViewingStatisticsDTO statistics = ViewingStatisticsDTO.builder()
                .userId(10L)
                .totalVideosWatched(0L)
                .totalCompletedVideos(0L)
                .totalTimeWatched(0L)
                .averageCompletionRate(0.0)
                .build();

        when(watchHistoryService.getViewingStatistics(10L)).thenReturn(statistics);

        mockMvc.perform(get("/api/watch-history/statistics/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVideosWatched").value(0))
                .andExpect(jsonPath("$.totalCompletedVideos").value(0))
                .andExpect(jsonPath("$.totalTimeWatched").value(0))
                .andExpect(jsonPath("$.averageCompletionRate").value(0.0));
    }
}
