package com.stromae.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistDTO {

    private Long id;
    private Long userId;
    private Long videoId;
    private Long addedAt;

}
