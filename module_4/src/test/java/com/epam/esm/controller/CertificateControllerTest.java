package com.epam.esm.controller;

import com.epam.esm.Application;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.impl.CertificateServiceImpl;
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

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class CertificateControllerTest {
    private final static String BASE_URL = "/api/v1/certificates";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private Certificate certificate;

    @MockBean
    private CertificateServiceImpl mockCertificateService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Tag tag = new Tag();
        tag.setName("pick");
        certificate = new Certificate();
        certificate.setName("New Certificate");
        certificate.setDescription("New description for test");
        certificate.setPrice(100.0);
        certificate.setDuration(100);
        certificate.setTags(Collections.singletonList(tag));
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
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void deleteCertificate() throws Exception {
        when(mockCertificateService.getCertificateById(1L)).thenReturn(new Certificate());
        mockMvc
                .perform(delete(String.format("%s/1", BASE_URL)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void createCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void createCertificate() throws Exception {
        when(mockCertificateService.createCertificate(certificate)).thenReturn(certificate);
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void updateCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(put(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(put(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void updateCertificate() throws Exception {
        when(mockCertificateService.updateCertificate(certificate, 1L)).thenReturn(certificate);
        mockMvc
                .perform(put(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void updatePartCertificate_AccessDeniedException() throws Exception {
        mockMvc
                .perform(patch(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updatePartCertificate_AuthenticationCredentialsNotFoundException() throws Exception {
        mockMvc
                .perform(patch(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void updatePartCertificate() throws Exception {
        when(mockCertificateService.updatePartCertificate(certificate, 1L)).thenReturn(certificate);
        mockMvc
                .perform(patch(String.format("%s/1", BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isOk());
    }
}