package com.stromae.videoservice.mapper;

import com.stromae.videoservice.dto.VideoDTO;
import com.stromae.videoservice.entity.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    VideoDTO toDTO(Video video);

    Video toEntity(VideoDTO videoDTO);

}
