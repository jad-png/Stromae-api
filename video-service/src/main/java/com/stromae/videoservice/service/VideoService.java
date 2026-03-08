package com.stromae.videoservice.service;

import com.stromae.videoservice.dto.VideoDTO;
import com.stromae.videoservice.entity.Video;
import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.entity.VideoType;
import com.stromae.videoservice.exception.ResourceNotFoundException;
import com.stromae.videoservice.mapper.VideoMapper;
import com.stromae.videoservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoDTO createVideo(VideoDTO videoDTO) {
        log.info("Creating video: {}", videoDTO.getTitle());
        Video video = videoMapper.toEntity(videoDTO);
        Video savedVideo = videoRepository.save(video);
        return videoMapper.toDTO(savedVideo);
    }

    @Transactional(readOnly = true)
    public VideoDTO getVideoById(Long id) {
        log.info("Fetching video with id: {}", id);
        return videoRepository.findById(id)
                .map(videoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> getAllVideos() {
        log.info("Fetching all videos");
        return videoRepository.findAll().stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VideoDTO updateVideo(Long id, VideoDTO videoDTO) {
        log.info("Updating video with id: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));

        if (videoDTO.getTitle() != null) video.setTitle(videoDTO.getTitle());
        if (videoDTO.getDescription() != null) video.setDescription(videoDTO.getDescription());
        if (videoDTO.getThumbnailUrl() != null) video.setThumbnailUrl(videoDTO.getThumbnailUrl());
        if (videoDTO.getTrailerUrl() != null) video.setTrailerUrl(videoDTO.getTrailerUrl());
        if (videoDTO.getDuration() != null) video.setDuration(videoDTO.getDuration());
        if (videoDTO.getReleaseYear() != null) video.setReleaseYear(videoDTO.getReleaseYear());
        if (videoDTO.getType() != null) video.setType(videoDTO.getType());
        if (videoDTO.getCategory() != null) video.setCategory(videoDTO.getCategory());
        if (videoDTO.getRating() != null) video.setRating(videoDTO.getRating());
        if (videoDTO.getDirector() != null) video.setDirector(videoDTO.getDirector());
        if (videoDTO.getCast() != null) video.setCast(videoDTO.getCast());

        Video updatedVideo = videoRepository.save(video);
        return videoMapper.toDTO(updatedVideo);
    }

    public void deleteVideo(Long id) {
        log.info("Deleting video with id: {}", id);
        if (!videoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Video not found with id: " + id);
        }
        videoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> getVideosByCategory(VideoCategory category) {
        log.info("Fetching videos by category: {}", category);
        return videoRepository.findByCategory(category).stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> getVideosByType(VideoType type) {
        log.info("Fetching videos by type: {}", type);
        return videoRepository.findByType(type).stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> searchByTitle(String title) {
        log.info("Searching videos by title: {}", title);
        return videoRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> searchByDirector(String director) {
        log.info("Searching videos by director: {}", director);
        return videoRepository.findByDirectorContainingIgnoreCase(director).stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

}
