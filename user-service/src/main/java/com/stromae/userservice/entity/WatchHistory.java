package com.stromae.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watch_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Column(name = "watched_at")
    private Long watchedAt;

    @Column(name = "progress_time")
    private Integer progressTime; // in seconds

    @Column(nullable = false)
    private Boolean completed;

    @PrePersist
    protected void onCreate() {
        watchedAt = System.currentTimeMillis();
        if (completed == null) completed = false;
    }

}
