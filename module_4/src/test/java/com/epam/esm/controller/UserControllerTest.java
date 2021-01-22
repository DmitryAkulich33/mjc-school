package com.epam.esm.controller;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private final static String BASE_URL = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void getUsers() throws Exception {
        mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void getUsers_AccessDeniedException() throws Exception {
        mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUsers_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserById_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(String.format("%s/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void getUserById() throws Exception {
        User user = new User();
        Role role = new Role();
        role.setName("ROLE_USER");

        user.setRoles(Collections.singletonList(role));

        when(mockUserService.getUserById(1L)).thenReturn(user);
        mockMvc
                .perform(get(String.format("%s/1", BASE_URL)))
                .andExpect(status().isOk());
    }
}