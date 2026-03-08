package com.stromae.userservice.mapper;

import com.stromae.userservice.dto.UserDTO;
import com.stromae.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

}
