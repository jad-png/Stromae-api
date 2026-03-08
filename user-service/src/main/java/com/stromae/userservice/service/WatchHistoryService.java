package com.stromae.userservice.service;

import com.stromae.userservice.dto.ViewingStatisticsDTO;
import com.stromae.userservice.dto.WatchHistoryDTO;
import com.stromae.userservice.entity.WatchHistory;
import com.stromae.userservice.feign.VideoServiceClient;
import com.stromae.userservice.mapper.WatchHistoryMapper;
import com.stromae.userservice.repository.WatchHistoryRepository;
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
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final WatchHistoryMapper watchHistoryMapper;
    private final VideoServiceClient videoServiceClient;

    public WatchHistoryDTO saveWatchHistory(WatchHistoryDTO watchHistoryDTO) {
        log.info("Saving watch history for user {} and video {}", watchHistoryDTO.getUserId(), watchHistoryDTO.getVideoId());

        // Verify video exists
        try {
            videoServiceClient.getVideoById(watchHistoryDTO.getVideoId());
        } catch (Exception e) {
            throw new RuntimeException("Video not found with id: " + watchHistoryDTO.getVideoId());
        }

        WatchHistory watchHistory = watchHistoryRepository.findByUserIdAndVideoId(
                watchHistoryDTO.getUserId(),
                watchHistoryDTO.getVideoId()
        ).orElse(watchHistoryMapper.toEntity(watchHistoryDTO));

        if (watchHistoryDTO.getProgressTime() != null) {
            watchHistory.setProgressTime(watchHistoryDTO.getProgressTime());
        }
        if (watchHistoryDTO.getCompleted() != null) {
            watchHistory.setCompleted(watchHistoryDTO.getCompleted());
        }

        WatchHistory savedWatchHistory = watchHistoryRepository.save(watchHistory);
        return watchHistoryMapper.toDTO(savedWatchHistory);
    }

    @Transactional(readOnly = true)
    public List<WatchHistoryDTO> getUserWatchHistory(Long userId) {
        log.info("Fetching watch history for user: {}", userId);
        return watchHistoryRepository.findByUserId(userId).stream()
                .map(watchHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WatchHistoryDTO getWatchHistoryByVideoId(Long userId, Long videoId) {
        log.info("Fetching watch history for user {} and video {}", userId, videoId);
        return watchHistoryRepository.findByUserIdAndVideoId(userId, videoId)
                .map(watchHistoryMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Watch history not found"));
    }

    @Transactional(readOnly = true)
    public ViewingStatisticsDTO getViewingStatistics(Long userId) {
        log.info("Calculating viewing statistics for user: {}", userId);

        Long totalVideosWatched = watchHistoryRepository.countDistinctVideosByUserId(userId);
        Long totalCompletedVideos = watchHistoryRepository.countCompletedVideosByUserId(userId);
        Long sumProgressTime = watchHistoryRepository.sumProgressTimeByUserId(userId);
        Long totalTimeWatched = sumProgressTime != null ? sumProgressTime : 0L;

        Double averageCompletionRate = totalVideosWatched > 0 ?
                (double) totalCompletedVideos / totalVideosWatched * 100 : 0.0;

        return ViewingStatisticsDTO.builder()
                .userId(userId)
                .totalVideosWatched(totalVideosWatched != null ? totalVideosWatched : 0L)
                .totalCompletedVideos(totalCompletedVideos != null ? totalCompletedVideos : 0L)
                .totalTimeWatched(totalTimeWatched)
                .averageCompletionRate(averageCompletionRate)
                .build();
    }

}
