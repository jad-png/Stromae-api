package com.stromae.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stromae.userservice.dto.WatchlistDTO;
import com.stromae.userservice.service.WatchlistService;
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

@WebMvcTest(WatchlistController.class)
class WatchlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WatchlistService watchlistService;

    @Autowired
    private ObjectMapper objectMapper;

    private WatchlistDTO watchlistDTO;

    @BeforeEach
    void setUp() {
        watchlistDTO = WatchlistDTO.builder()
                .id(1L)
                .userId(10L)
                .videoId(20L)
                .addedAt(1700000000000L)
                .build();
    }

    @Test
    void addToWatchlist_ReturnsCreated() throws Exception {
        when(watchlistService.addToWatchlist(any(WatchlistDTO.class))).thenReturn(watchlistDTO);

        mockMvc.perform(post("/api/watchlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(watchlistDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.videoId").value(20L))
                .andExpect(jsonPath("$.addedAt").value(1700000000000L));

        verify(watchlistService, times(1)).addToWatchlist(any(WatchlistDTO.class));
    }

    @Test
    void addToWatchlist_VideoNotFound_ReturnsServerError() throws Exception {
        when(watchlistService.addToWatchlist(any(WatchlistDTO.class)))
                .thenThrow(new RuntimeException("Video not found with id: 20"));

        mockMvc.perform(post("/api/watchlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(watchlistDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addToWatchlist_AlreadyExists_ReturnsServerError() throws Exception {
        when(watchlistService.addToWatchlist(any(WatchlistDTO.class)))
                .thenThrow(new RuntimeException("Video already in watchlist"));

        mockMvc.perform(post("/api/watchlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(watchlistDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getUserWatchlist_ReturnsList() throws Exception {
        WatchlistDTO dto2 = WatchlistDTO.builder()
                .id(2L).userId(10L).videoId(30L).addedAt(1700000001000L).build();
        List<WatchlistDTO> watchlist = Arrays.asList(watchlistDTO, dto2);

        when(watchlistService.getUserWatchlist(10L)).thenReturn(watchlist);

        mockMvc.perform(get("/api/watchlist/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].videoId").value(20L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].videoId").value(30L));

        verify(watchlistService, times(1)).getUserWatchlist(10L);
    }

    @Test
    void getUserWatchlist_EmptyList() throws Exception {
        when(watchlistService.getUserWatchlist(10L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/watchlist/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void removeFromWatchlist_ReturnsNoContent() throws Exception {
        doNothing().when(watchlistService).removeFromWatchlist(10L, 20L);

        mockMvc.perform(delete("/api/watchlist/10/20"))
                .andExpect(status().isNoContent());

        verify(watchlistService, times(1)).removeFromWatchlist(10L, 20L);
    }

    @Test
    void isVideoInWatchlist_ReturnsTrue() throws Exception {
        when(watchlistService.isVideoInWatchlist(10L, 20L)).thenReturn(true);

        mockMvc.perform(get("/api/watchlist/10/20"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(watchlistService, times(1)).isVideoInWatchlist(10L, 20L);
    }

    @Test
    void isVideoInWatchlist_ReturnsFalse() throws Exception {
        when(watchlistService.isVideoInWatchlist(10L, 20L)).thenReturn(false);

        mockMvc.perform(get("/api/watchlist/10/20"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
