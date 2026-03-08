package com.stromae.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watchlists", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "video_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Column(name = "added_at")
    private Long addedAt;

    @PrePersist
    protected void onCreate() {
        addedAt = System.currentTimeMillis();
    }

}
