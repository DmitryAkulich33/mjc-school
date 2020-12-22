package com.epam.esm.controller;

import com.epam.esm.Application;
import com.epam.esm.domain.*;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class OrderControllerTest {
    private final static String BASE_URL = "/api/v1/orders";
    private List<Certificate> certificates;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private Order order;

    @MockBean
    private OrderServiceImpl mockOrderService;

    @BeforeEach
    public void setUp() {
        Tag tag = new Tag();
        User user = new User();
        Role role = new Role();
        Certificate certificate = new Certificate();
        certificate.setId(1L);
        certificate.setTags(Collections.singletonList(tag));
        certificates = new ArrayList<>(Collections.singletonList(certificate));
        order = new Order();
        user.setRoles(Collections.singletonList(role));
        order.setUser(user);
        order.setCertificates(certificates);
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
    public void getOrderById() throws Exception {
        when(mockOrderService.getOrderById(1L)).thenReturn(order);
        mockMvc
                .perform(get(String.format("%s/1", BASE_URL)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void makeOrder_AccessDeniedException() throws Exception {
        mockMvc
                .perform(post(String.format("%s/users/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificates)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void makeOrder_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(post(String.format("%s/users/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificates)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getDataByUserId_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(get(String.format("%s/1/users/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void getDataByUserId() throws Exception {
        when(mockOrderService.getOrderDataByUserId(1L, 1L)).thenReturn(order);
        mockMvc
                .perform(get(String.format("%s/1/users/1", BASE_URL)))
                .andExpect(status().isOk());
    }
}