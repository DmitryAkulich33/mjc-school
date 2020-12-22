package com.epam.esm.controller;

import com.epam.esm.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class OrderControllerTest {
    private final static String ORDER = "[\n" +
            "    {\n" +
            "        \"id\" : 1\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\" : 2\n" +
            "    }\n" +
            "]";
    private final static String BASE_URL = "/api/v1/orders";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void getOrders() throws Exception {
        mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void getOrders_AccessDeniedException() throws Exception {
        mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getOrders_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getOrdersByUserId_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(String.format("%s/users/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void getOrdersByUserId() throws Exception {
        mockMvc
                .perform(get(String.format("%s/users/1", BASE_URL)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void getOrderById_AccessDeniedException() throws Exception {
        mockMvc
                .perform(get(String.format("%s/1", BASE_URL)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getOrderById_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(String.format("%s/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void makeOrder_AccessDeniedException() throws Exception {
        mockMvc
                .perform(post(String.format("%s/users/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ORDER))
                .andExpect(status().isForbidden());
    }

    @Test
    public void makeOrder_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(post(String.format("%s/users/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ORDER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getDataByUserId_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(String.format("%s/1/users/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }
}