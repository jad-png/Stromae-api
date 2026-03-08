package com.stromae.userservice.service;

import com.stromae.userservice.dto.WatchlistDTO;
import com.stromae.userservice.entity.Watchlist;
import com.stromae.userservice.feign.VideoServiceClient;
import com.stromae.userservice.mapper.WatchlistMapper;
import com.stromae.userservice.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistMapper watchlistMapper;
    private final VideoServiceClient videoServiceClient;

    public WatchlistDTO addToWatchlist(WatchlistDTO watchlistDTO) {
        log.info("Adding video {} to watchlist for user {}", watchlistDTO.getVideoId(), watchlistDTO.getUserId());

        // Verify video exists
        try {
            videoServiceClient.getVideoById(watchlistDTO.getVideoId());
        } catch (Exception e) {
            throw new RuntimeException("Video not found with id: " + watchlistDTO.getVideoId());
        }

        if (watchlistRepository.findByUserIdAndVideoId(watchlistDTO.getUserId(), watchlistDTO.getVideoId()).isPresent()) {
            throw new RuntimeException("Video already in watchlist");
        }

        Watchlist watchlist = watchlistMapper.toEntity(watchlistDTO);
        Watchlist savedWatchlist = watchlistRepository.save(watchlist);
        return watchlistMapper.toDTO(savedWatchlist);
    }

    @Transactional(readOnly = true)
    public List<WatchlistDTO> getUserWatchlist(Long userId) {
        log.info("Fetching watchlist for user: {}", userId);
        return watchlistRepository.findByUserId(userId).stream()
                .map(watchlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void removeFromWatchlist(Long userId, Long videoId) {
        log.info("Removing video {} from watchlist for user {}", videoId, userId);
        watchlistRepository.deleteByUserIdAndVideoId(userId, videoId);
    }

    @Transactional(readOnly = true)
    public boolean isVideoInWatchlist(Long userId, Long videoId) {
        log.info("Checking if video {} is in watchlist for user {}", videoId, userId);
        return watchlistRepository.findByUserIdAndVideoId(userId, videoId).isPresent();
    }

}
