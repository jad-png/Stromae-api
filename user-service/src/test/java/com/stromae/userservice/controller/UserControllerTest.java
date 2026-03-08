package com.stromae.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stromae.userservice.dto.UserDTO;
import com.stromae.userservice.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .createdAt(1700000000000L)
                .updatedAt(1700000000000L)
                .build();
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    void createUser_withInvalidData_shouldReturn400() throws Exception {
        UserDTO invalidDTO = UserDTO.builder().build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDTO.class));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService).getUserById(1L);
    }

    @Test
    void getUserById_notFound_shouldReturn500() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new RuntimeException("User not found with id: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().is5xxServerError());

        verify(userService).getUserById(99L);
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        UserDTO secondUser = UserDTO.builder()
                .id(2L)
                .username("anotheruser")
                .email("another@example.com")
                .password("pass456")
                .build();
        List<UserDTO> users = Arrays.asList(userDTO, secondUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[1].username", is("anotheruser")));

        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_empty_shouldReturnEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserByUsername_shouldReturnUser() throws Exception {
        when(userService.getUserByUsername("testuser")).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService).getUserByUsername("testuser");
    }

    @Test
    void getUserByUsername_notFound_shouldReturn500() throws Exception {
        when(userService.getUserByUsername("unknown"))
                .thenThrow(new RuntimeException("User not found with username: unknown"));

        mockMvc.perform(get("/api/users/username/unknown"))
                .andExpect(status().is5xxServerError());

        verify(userService).getUserByUsername("unknown");
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserDTO updatedDTO = UserDTO.builder()
                .id(1L)
                .username("updateduser")
                .email("updated@example.com")
                .password("newpass123")
                .build();
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updateduser")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(userService).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    void updateUser_withInvalidData_shouldReturn400() throws Exception {
        UserDTO invalidDTO = UserDTO.builder().build();

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(anyLong(), any(UserDTO.class));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void deleteUser_notFound_shouldReturn500() throws Exception {
        doThrow(new RuntimeException("User not found with id: 99"))
                .when(userService).deleteUser(99L);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().is5xxServerError());

        verify(userService).deleteUser(99L);
    }

}
