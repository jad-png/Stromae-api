package com.stromae.videoservice.mapper;

import com.stromae.videoservice.dto.VideoDTO;
import com.stromae.videoservice.entity.Video;
import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.entity.VideoType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class VideoMapperTest {

    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);

    @Test
    void toDTO_shouldMapAllFields() {
        Video video = Video.builder()
                .id(1L)
                .title("Inception")
                .description("A mind-bending thriller")
                .thumbnailUrl("http://img.com/inception.jpg")
                .trailerUrl("http://trailer.com/inception.mp4")
                .duration(148)
                .releaseYear(2010)
                .type(VideoType.FILM)
                .category(VideoCategory.SCIENCE_FICTION)
                .rating(8.8)
                .director("Christopher Nolan")
                .cast("Leonardo DiCaprio, Tom Hardy")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();

        VideoDTO dto = videoMapper.toDTO(video);

        assertEquals(1L, dto.getId());
        assertEquals("Inception", dto.getTitle());
        assertEquals("A mind-bending thriller", dto.getDescription());
        assertEquals("http://img.com/inception.jpg", dto.getThumbnailUrl());
        assertEquals("http://trailer.com/inception.mp4", dto.getTrailerUrl());
        assertEquals(148, dto.getDuration());
        assertEquals(2010, dto.getReleaseYear());
        assertEquals(VideoType.FILM, dto.getType());
        assertEquals(VideoCategory.SCIENCE_FICTION, dto.getCategory());
        assertEquals(8.8, dto.getRating());
        assertEquals("Christopher Nolan", dto.getDirector());
        assertEquals("Leonardo DiCaprio, Tom Hardy", dto.getCast());
        assertEquals(1700000000000L, dto.getCreatedAt());
        assertEquals(1700000000000L, dto.getUpdatedAt());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        VideoDTO dto = VideoDTO.builder()
                .id(2L)
                .title("Interstellar")
                .description("Space exploration epic")
                .thumbnailUrl("http://img.com/interstellar.jpg")
                .trailerUrl("http://trailer.com/interstellar.mp4")
                .duration(169)
                .releaseYear(2014)
                .type(VideoType.FILM)
                .category(VideoCategory.DRAME)
                .rating(8.6)
                .director("Christopher Nolan")
                .cast("Matthew McConaughey, Anne Hathaway")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();

        Video video = videoMapper.toEntity(dto);

        assertEquals(2L, video.getId());
        assertEquals("Interstellar", video.getTitle());
        assertEquals("Space exploration epic", video.getDescription());
        assertEquals("http://img.com/interstellar.jpg", video.getThumbnailUrl());
        assertEquals("http://trailer.com/interstellar.mp4", video.getTrailerUrl());
        assertEquals(169, video.getDuration());
        assertEquals(2014, video.getReleaseYear());
        assertEquals(VideoType.FILM, video.getType());
        assertEquals(VideoCategory.DRAME, video.getCategory());
        assertEquals(8.6, video.getRating());
        assertEquals("Christopher Nolan", video.getDirector());
        assertEquals("Matthew McConaughey, Anne Hathaway", video.getCast());
        assertEquals(1700000000000L, video.getCreatedAt());
        assertEquals(1700000000000L, video.getUpdatedAt());
    }

    @Test
    void toDTO_withNullEntity_shouldReturnNull() {
        assertNull(videoMapper.toDTO(null));
    }

    @Test
    void toEntity_withNullDTO_shouldReturnNull() {
        assertNull(videoMapper.toEntity(null));
    }

    @Test
    void toDTO_withNullOptionalFields_shouldMapNulls() {
        Video video = Video.builder()
                .id(1L)
                .title("Minimal")
                .duration(90)
                .type(VideoType.SERIE)
                .category(VideoCategory.COMEDIE)
                .build();

        VideoDTO dto = videoMapper.toDTO(video);

        assertEquals(1L, dto.getId());
        assertEquals("Minimal", dto.getTitle());
        assertEquals(90, dto.getDuration());
        assertEquals(VideoType.SERIE, dto.getType());
        assertEquals(VideoCategory.COMEDIE, dto.getCategory());
        assertNull(dto.getDescription());
        assertNull(dto.getThumbnailUrl());
        assertNull(dto.getTrailerUrl());
        assertNull(dto.getReleaseYear());
        assertNull(dto.getRating());
        assertNull(dto.getDirector());
        assertNull(dto.getCast());
    }

    @Test
    void toEntity_shouldMapEnumsCorrectly() {
        VideoDTO dto = VideoDTO.builder()
                .id(1L)
                .title("Horror Film")
                .duration(100)
                .type(VideoType.FILM)
                .category(VideoCategory.HORREUR)
                .build();

        Video video = videoMapper.toEntity(dto);

        assertEquals(VideoType.FILM, video.getType());
        assertEquals(VideoCategory.HORREUR, video.getCategory());
    }

    @Test
    void roundTrip_shouldPreserveData() {
        VideoDTO original = VideoDTO.builder()
                .id(10L)
                .title("Round Trip Movie")
                .description("Testing round trip")
                .thumbnailUrl("http://thumb.com")
                .trailerUrl("http://trailer.com")
                .duration(120)
                .releaseYear(2023)
                .type(VideoType.SERIE)
                .category(VideoCategory.THRILLER)
                .rating(7.5)
                .director("Test Director")
                .cast("Actor A, Actor B")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();

        Video entity = videoMapper.toEntity(original);
        VideoDTO result = videoMapper.toDTO(entity);

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getTitle(), result.getTitle());
        assertEquals(original.getDescription(), result.getDescription());
        assertEquals(original.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(original.getTrailerUrl(), result.getTrailerUrl());
        assertEquals(original.getDuration(), result.getDuration());
        assertEquals(original.getReleaseYear(), result.getReleaseYear());
        assertEquals(original.getType(), result.getType());
        assertEquals(original.getCategory(), result.getCategory());
        assertEquals(original.getRating(), result.getRating());
        assertEquals(original.getDirector(), result.getDirector());
        assertEquals(original.getCast(), result.getCast());
        assertEquals(original.getCreatedAt(), result.getCreatedAt());
        assertEquals(original.getUpdatedAt(), result.getUpdatedAt());
    }

}
