package com.stromae.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistoryDTO {

    private Long id;
    private Long userId;
    private Long videoId;
    private Long watchedAt;
    private Integer progressTime;
    private Boolean completed;

}
