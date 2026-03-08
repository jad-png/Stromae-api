package com.stromae.userservice.repository;

import com.stromae.userservice.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    List<WatchHistory> findByUserId(Long userId);

    Optional<WatchHistory> findByUserIdAndVideoId(Long userId, Long videoId);

    @Query("SELECT COUNT(DISTINCT wh.videoId) FROM WatchHistory wh WHERE wh.userId = ?1")
    Long countDistinctVideosByUserId(Long userId);

    @Query("SELECT COUNT(wh) FROM WatchHistory wh WHERE wh.userId = ?1 AND wh.completed = true")
    Long countCompletedVideosByUserId(Long userId);

    @Query("SELECT SUM(wh.progressTime) FROM WatchHistory wh WHERE wh.userId = ?1")
    Long sumProgressTimeByUserId(Long userId);

}
