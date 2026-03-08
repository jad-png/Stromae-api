package com.stromae.videoservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stromae.videoservice.dto.VideoDTO;
import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.entity.VideoType;
import com.stromae.videoservice.exception.ResourceNotFoundException;
import com.stromae.videoservice.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoController.class)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    private VideoDTO videoDTO;

    @BeforeEach
    void setUp() {
        videoDTO = VideoDTO.builder()
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
    }

    @Test
    void createVideo_shouldReturnCreatedVideo() throws Exception {
        when(videoService.createVideo(any(VideoDTO.class))).thenReturn(videoDTO);

        mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(videoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Inception")))
                .andExpect(jsonPath("$.type", is("FILM")))
                .andExpect(jsonPath("$.category", is("SCIENCE_FICTION")));

        verify(videoService).createVideo(any(VideoDTO.class));
    }

    @Test
    void createVideo_withMissingRequiredFields_shouldReturn400() throws Exception {
        VideoDTO invalidDTO = VideoDTO.builder()
                .description("No title, no duration, no type, no category")
                .build();

        mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(videoService, never()).createVideo(any(VideoDTO.class));
    }

    @Test
    void getVideoById_shouldReturnVideo() throws Exception {
        when(videoService.getVideoById(1L)).thenReturn(videoDTO);

        mockMvc.perform(get("/api/videos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Inception")))
                .andExpect(jsonPath("$.director", is("Christopher Nolan")));

        verify(videoService).getVideoById(1L);
    }

    @Test
    void getVideoById_notFound_shouldReturn404() throws Exception {
        when(videoService.getVideoById(99L))
                .thenThrow(new ResourceNotFoundException("Video not found with id: 99"));

        mockMvc.perform(get("/api/videos/99"))
                .andExpect(status().isNotFound());

        verify(videoService).getVideoById(99L);
    }

    @Test
    void getAllVideos_shouldReturnList() throws Exception {
        VideoDTO secondVideo = VideoDTO.builder()
                .id(2L)
                .title("Interstellar")
                .duration(169)
                .type(VideoType.FILM)
                .category(VideoCategory.SCIENCE_FICTION)
                .build();
        List<VideoDTO> videos = Arrays.asList(videoDTO, secondVideo);
        when(videoService.getAllVideos()).thenReturn(videos);

        mockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Inception")))
                .andExpect(jsonPath("$[1].title", is("Interstellar")));

        verify(videoService).getAllVideos();
    }

    @Test
    void getAllVideos_empty_shouldReturnEmptyList() throws Exception {
        when(videoService.getAllVideos()).thenReturn(List.of());

        mockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(videoService).getAllVideos();
    }

    @Test
    void updateVideo_shouldReturnUpdatedVideo() throws Exception {
        VideoDTO updatePayload = VideoDTO.builder()
                .title("Inception Updated")
                .rating(9.0)
                .build();
        VideoDTO updatedResult = VideoDTO.builder()
                .id(1L)
                .title("Inception Updated")
                .description("A mind-bending thriller")
                .duration(148)
                .type(VideoType.FILM)
                .category(VideoCategory.SCIENCE_FICTION)
                .rating(9.0)
                .director("Christopher Nolan")
                .build();
        when(videoService.updateVideo(eq(1L), any(VideoDTO.class))).thenReturn(updatedResult);

        mockMvc.perform(put("/api/videos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Inception Updated")))
                .andExpect(jsonPath("$.rating", is(9.0)));

        verify(videoService).updateVideo(eq(1L), any(VideoDTO.class));
    }

    @Test
    void updateVideo_notFound_shouldReturn404() throws Exception {
        VideoDTO updatePayload = VideoDTO.builder().title("Does not matter").build();
        when(videoService.updateVideo(eq(99L), any(VideoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Video not found with id: 99"));

        mockMvc.perform(put("/api/videos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isNotFound());

        verify(videoService).updateVideo(eq(99L), any(VideoDTO.class));
    }

    @Test
    void deleteVideo_shouldReturn204() throws Exception {
        doNothing().when(videoService).deleteVideo(1L);

        mockMvc.perform(delete("/api/videos/1"))
                .andExpect(status().isNoContent());

        verify(videoService).deleteVideo(1L);
    }

    @Test
    void deleteVideo_notFound_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Video not found with id: 99"))
                .when(videoService).deleteVideo(99L);

        mockMvc.perform(delete("/api/videos/99"))
                .andExpect(status().isNotFound());

        verify(videoService).deleteVideo(99L);
    }

    @Test
    void getVideosByCategory_shouldReturnFilteredList() throws Exception {
        when(videoService.getVideosByCategory(VideoCategory.ACTION)).thenReturn(List.of(videoDTO));

        mockMvc.perform(get("/api/videos/category/ACTION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(videoService).getVideosByCategory(VideoCategory.ACTION);
    }

    @Test
    void getVideosByCategory_empty_shouldReturnEmptyList() throws Exception {
        when(videoService.getVideosByCategory(VideoCategory.HORREUR)).thenReturn(List.of());

        mockMvc.perform(get("/api/videos/category/HORREUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(videoService).getVideosByCategory(VideoCategory.HORREUR);
    }

    @Test
    void getVideosByType_shouldReturnFilteredList() throws Exception {
        when(videoService.getVideosByType(VideoType.FILM)).thenReturn(List.of(videoDTO));

        mockMvc.perform(get("/api/videos/type/FILM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("FILM")));

        verify(videoService).getVideosByType(VideoType.FILM);
    }

    @Test
    void getVideosByType_empty_shouldReturnEmptyList() throws Exception {
        when(videoService.getVideosByType(VideoType.SERIE)).thenReturn(List.of());

        mockMvc.perform(get("/api/videos/type/SERIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(videoService).getVideosByType(VideoType.SERIE);
    }

    @Test
    void searchByTitle_shouldReturnMatchingVideos() throws Exception {
        when(videoService.searchByTitle("incep")).thenReturn(List.of(videoDTO));

        mockMvc.perform(get("/api/videos/search/title").param("query", "incep"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Inception")));

        verify(videoService).searchByTitle("incep");
    }

    @Test
    void searchByTitle_noMatch_shouldReturnEmptyList() throws Exception {
        when(videoService.searchByTitle("nonexistent")).thenReturn(List.of());

        mockMvc.perform(get("/api/videos/search/title").param("query", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(videoService).searchByTitle("nonexistent");
    }

    @Test
    void searchByDirector_shouldReturnMatchingVideos() throws Exception {
        when(videoService.searchByDirector("Nolan")).thenReturn(List.of(videoDTO));

        mockMvc.perform(get("/api/videos/search/director").param("query", "Nolan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].director", is("Christopher Nolan")));

        verify(videoService).searchByDirector("Nolan");
    }

    @Test
    void searchByDirector_noMatch_shouldReturnEmptyList() throws Exception {
        when(videoService.searchByDirector("Unknown")).thenReturn(List.of());

        mockMvc.perform(get("/api/videos/search/director").param("query", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(videoService).searchByDirector("Unknown");
    }

}
