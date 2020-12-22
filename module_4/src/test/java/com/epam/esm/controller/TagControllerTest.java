package com.epam.esm.controller;

import com.epam.esm.Application;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.impl.TagServiceImpl;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class TagControllerTest {
    private final static String BASE_URL = "/api/v1/tags";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private Tag tag;

    @MockBean
    private TagServiceImpl mockTagService;

    @BeforeEach
    public void setUp() {
        tag = new Tag();
        tag.setName("test");
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void deleteTag_AccessDeniedException() throws Exception {
        mockMvc
                .perform(delete(String.format("%s/1", BASE_URL)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteTag_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(delete(String.format("%s/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void deleteTag() throws Exception {
        when(mockTagService.getTagById(1L)).thenReturn(new Tag());
        mockMvc
                .perform(delete(String.format("%s/1", BASE_URL)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void createTag_AccessDeniedException() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createTag_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void createTag() throws Exception {
        when(mockTagService.createTag(tag)).thenReturn(tag);
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated());
    }
}