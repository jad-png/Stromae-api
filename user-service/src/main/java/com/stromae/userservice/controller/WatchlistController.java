package com.stromae.userservice.controller;

import com.stromae.userservice.dto.WatchlistDTO;
import com.stromae.userservice.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Slf4j
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping
    public ResponseEntity<WatchlistDTO> addToWatchlist(@Valid @RequestBody WatchlistDTO watchlistDTO) {
        log.info("Adding video {} to watchlist for user {}", watchlistDTO.getVideoId(), watchlistDTO.getUserId());
        WatchlistDTO addedItem = watchlistService.addToWatchlist(watchlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchlistDTO>> getUserWatchlist(@PathVariable Long userId) {
        log.info("Fetching watchlist for user: {}", userId);
        List<WatchlistDTO> watchlist = watchlistService.getUserWatchlist(userId);
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/{userId}/{videoId}")
    public ResponseEntity<Void> removeFromWatchlist(@PathVariable Long userId, @PathVariable Long videoId) {
        log.info("Removing video {} from watchlist for user {}", videoId, userId);
        watchlistService.removeFromWatchlist(userId, videoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/{videoId}")
    public ResponseEntity<Boolean> isVideoInWatchlist(@PathVariable Long userId, @PathVariable Long videoId) {
        log.info("Checking if video {} is in watchlist for user {}", videoId, userId);
        boolean isInWatchlist = watchlistService.isVideoInWatchlist(userId, videoId);
        return ResponseEntity.ok(isInWatchlist);
    }

}
