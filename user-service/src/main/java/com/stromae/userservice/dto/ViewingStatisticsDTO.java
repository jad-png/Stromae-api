package com.stromae.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewingStatisticsDTO {

    private Long userId;
    private Long totalVideosWatched;
    private Long totalCompletedVideos;
    private Long totalTimeWatched; // in seconds
    private Double averageCompletionRate;

}
