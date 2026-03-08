package com.stromae.userservice.mapper;

import com.stromae.userservice.dto.UserDTO;
import com.stromae.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDTO_shouldMapAllFields() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();

        UserDTO dto = userMapper.toDTO(user);

        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
        assertEquals(1700000000000L, dto.getCreatedAt());
        assertEquals(1700000000000L, dto.getUpdatedAt());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UserDTO dto = UserDTO.builder()
                .id(2L)
                .username("newuser")
                .email("new@example.com")
                .password("newpass")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();

        User user = userMapper.toEntity(dto);

        assertEquals(2L, user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
        assertEquals(1700000000000L, user.getCreatedAt());
        assertEquals(1700000000000L, user.getUpdatedAt());
    }

    @Test
    void toDTO_withNullEntity_shouldReturnNull() {
        assertNull(userMapper.toDTO(null));
    }

    @Test
    void toEntity_withNullDTO_shouldReturnNull() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    void toDTO_withNullFields_shouldMapNulls() {
        User user = User.builder().id(1L).build();

        UserDTO dto = userMapper.toDTO(user);

        assertEquals(1L, dto.getId());
        assertNull(dto.getUsername());
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void roundTrip_shouldPreserveData() {
        UserDTO original = UserDTO.builder()
                .id(5L)
                .username("roundtrip")
                .email("round@trip.com")
                .password("secret")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();

        User entity = userMapper.toEntity(original);
        UserDTO result = userMapper.toDTO(entity);

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getUsername(), result.getUsername());
        assertEquals(original.getEmail(), result.getEmail());
        assertEquals(original.getPassword(), result.getPassword());
        assertEquals(original.getCreatedAt(), result.getCreatedAt());
        assertEquals(original.getUpdatedAt(), result.getUpdatedAt());
    }

}
