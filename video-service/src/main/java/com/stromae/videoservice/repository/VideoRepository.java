package com.stromae.videoservice.repository;

import com.stromae.videoservice.entity.Video;
import com.stromae.videoservice.entity.VideoCategory;
import com.stromae.videoservice.entity.VideoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByCategory(VideoCategory category);

    List<Video> findByType(VideoType type);

    List<Video> findByTitleContainingIgnoreCase(String title);

    List<Video> findByDirectorContainingIgnoreCase(String director);

    List<Video> findByCategoryAndType(VideoCategory category, VideoType type);

}
