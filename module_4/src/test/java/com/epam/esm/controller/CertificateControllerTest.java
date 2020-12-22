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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class CertificateControllerTest {
    private final static String CERTIFICATE = "{\n" +
            "    \"name\" : \"New Certificate\",\n" +
            "    \"description\" : \"New description for test\",\n" +
            "    \"price\" : \"100.0\",\n" +
            "    \"duration\" : \"100\",\n" +
            "    \"tags\" : [\n" +
            "        {\n" +
            "            \"name\" : \"pick\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\" : \"new tag\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\" : \"new tag\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private final static String BASE_URL = "/api/v1/certificates";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void deleteCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(delete(String.format("%s/1", BASE_URL)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(delete(String.format("%s/1", BASE_URL)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void createCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void createCertificate() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void updateCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(put(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(put(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void updatePartCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(patch(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updatePartCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(patch(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CERTIFICATE))
                .andExpect(status().isUnauthorized());
    }
}