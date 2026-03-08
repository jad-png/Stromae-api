package com.stromae.userservice.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long userId;

    @NotNull
    private Long videoId;

    private Long addedAt;

}
