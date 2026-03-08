package com.stromae.videoservice.service;

import com.stromae.videoservice.dto.VideoDTO;
import com.stromae.videoservice.entity.Video;
import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.mapper.VideoMapper;
import com.stromae.videoservice.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private VideoService videoService;

    private VideoDTO videoDTO;
    private Video video;

    @BeforeEach
    void setUp() {
        videoDTO = VideoDTO.builder()
                .id(1L)
                .title("Test Video")
                .description("Test Description")
                .duration(3600)
                .releaseYear(2023)
                .rating(8.5)
                .director("Test Director")
                .build();

        video = Video.builder()
                .id(1L)
                .title("Test Video")
                .description("Test Description")
                .duration(3600)
                .releaseYear(2023)
                .rating(8.5)
                .director("Test Director")
                .build();
    }

    @Test
    void testCreateVideo() {
        when(videoMapper.toEntity(videoDTO)).thenReturn(video);
        when(videoRepository.save(video)).thenReturn(video);
        when(videoMapper.toDTO(video)).thenReturn(videoDTO);

        VideoDTO result = videoService.createVideo(videoDTO);

        assertNotNull(result);
        assertEquals(videoDTO.getTitle(), result.getTitle());
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void testGetVideoById() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(videoMapper.toDTO(video)).thenReturn(videoDTO);

        VideoDTO result = videoService.getVideoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(videoRepository, times(1)).findById(1L);
    }

    @Test
    void testGetVideoByIdNotFound() {
        when(videoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> videoService.getVideoById(999L));
    }

    @Test
    void testDeleteVideo() {
        when(videoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(videoRepository).deleteById(1L);

        videoService.deleteVideo(1L);

        verify(videoRepository, times(1)).deleteById(1L);
    }

}
