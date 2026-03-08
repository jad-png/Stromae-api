package com.stromae.userservice.controller;

import com.stromae.userservice.dto.ViewingStatisticsDTO;
import com.stromae.userservice.dto.WatchHistoryDTO;
import com.stromae.userservice.service.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watch-history")
@RequiredArgsConstructor
@Slf4j
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @PostMapping
    public ResponseEntity<WatchHistoryDTO> saveWatchHistory(@RequestBody WatchHistoryDTO watchHistoryDTO) {
        log.info("Saving watch history for user {} and video {}", watchHistoryDTO.getUserId(), watchHistoryDTO.getVideoId());
        WatchHistoryDTO savedHistory = watchHistoryService.saveWatchHistory(watchHistoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHistory);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchHistoryDTO>> getUserWatchHistory(@PathVariable Long userId) {
        log.info("Fetching watch history for user: {}", userId);
        List<WatchHistoryDTO> history = watchHistoryService.getUserWatchHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/user/{userId}/video/{videoId}")
    public ResponseEntity<WatchHistoryDTO> getWatchHistoryByVideoId(
            @PathVariable Long userId,
            @PathVariable Long videoId) {
        log.info("Fetching watch history for user {} and video {}", userId, videoId);
        WatchHistoryDTO history = watchHistoryService.getWatchHistoryByVideoId(userId, videoId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/statistics/user/{userId}")
    public ResponseEntity<ViewingStatisticsDTO> getViewingStatistics(@PathVariable Long userId) {
        log.info("Fetching viewing statistics for user: {}", userId);
        ViewingStatisticsDTO statistics = watchHistoryService.getViewingStatistics(userId);
        return ResponseEntity.ok(statistics);
    }

}
