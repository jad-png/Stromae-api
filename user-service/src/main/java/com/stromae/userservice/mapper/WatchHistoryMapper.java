package com.stromae.userservice.mapper;

import com.stromae.userservice.dto.WatchHistoryDTO;
import com.stromae.userservice.entity.WatchHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchHistoryMapper {

    WatchHistoryDTO toDTO(WatchHistory watchHistory);

    WatchHistory toEntity(WatchHistoryDTO watchHistoryDTO);

}
