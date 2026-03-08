package com.stromae.videoservice.controller;

import com.stromae.videoservice.dto.OnCreate;
import com.stromae.videoservice.dto.OnUpdate;
import com.stromae.videoservice.dto.VideoDTO;
import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.entity.VideoType;
import com.stromae.videoservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<VideoDTO> createVideo(@Validated(OnCreate.class) @RequestBody VideoDTO videoDTO) {
        log.info("Creating video: {}", videoDTO.getTitle());
        VideoDTO createdVideo = videoService.createVideo(videoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable Long id) {
        log.info("Fetching video with id: {}", id);
        VideoDTO video = videoService.getVideoById(id);
        return ResponseEntity.ok(video);
    }

    @GetMapping
    public ResponseEntity<List<VideoDTO>> getAllVideos() {
        log.info("Fetching all videos");
        List<VideoDTO> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDTO> updateVideo(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody VideoDTO videoDTO) {
        log.info("Updating video with id: {}", id);
        VideoDTO updatedVideo = videoService.updateVideo(id, videoDTO);
        return ResponseEntity.ok(updatedVideo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        log.info("Deleting video with id: {}", id);
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<VideoDTO>> getVideosByCategory(@PathVariable VideoCategory category) {
        log.info("Fetching videos by category: {}", category);
        List<VideoDTO> videos = videoService.getVideosByCategory(category);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<VideoDTO>> getVideosByType(@PathVariable VideoType type) {
        log.info("Fetching videos by type: {}", type);
        List<VideoDTO> videos = videoService.getVideosByType(type);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<VideoDTO>> searchByTitle(@RequestParam String query) {
        log.info("Searching videos by title: {}", query);
        List<VideoDTO> videos = videoService.searchByTitle(query);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/search/director")
    public ResponseEntity<List<VideoDTO>> searchByDirector(@RequestParam String query) {
        log.info("Searching videos by director: {}", query);
        List<VideoDTO> videos = videoService.searchByDirector(query);
        return ResponseEntity.ok(videos);
    }

}
