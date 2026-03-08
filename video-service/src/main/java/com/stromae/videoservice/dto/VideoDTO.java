package com.stromae.videoservice.dto;

import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.entity.VideoType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDTO {

    private Long id;

    @NotBlank(groups = OnCreate.class)
    @Size(max = 255)
    private String title;

    @Size(max = 5000)
    private String description;

    private String thumbnailUrl;
    private String trailerUrl;

    @NotNull(groups = OnCreate.class)
    @Min(1)
    private Integer duration;

    @Min(1888)
    private Integer releaseYear;

    @NotNull(groups = OnCreate.class)
    private VideoType type;

    @NotNull(groups = OnCreate.class)
    private VideoCategory category;

    @Min(0)
    @Max(10)
    private Double rating;

    private String director;
    private String cast;
    private Long createdAt;
    private Long updatedAt;

}
