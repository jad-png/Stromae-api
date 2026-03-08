package com.stromae.userservice.mapper;

import com.stromae.userservice.dto.WatchlistDTO;
import com.stromae.userservice.entity.Watchlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchlistMapper {

    WatchlistDTO toDTO(Watchlist watchlist);

    Watchlist toEntity(WatchlistDTO watchlistDTO);

}
