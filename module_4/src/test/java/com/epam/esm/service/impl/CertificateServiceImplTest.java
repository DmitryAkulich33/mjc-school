package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.specification.CertificateSpecification;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateDuplicateException;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {
    private static final Long CERTIFICATE_ID = 1L;
    private static final String CERTIFICATE_NAME = "Certificate for one purchase";
    private static final String TAG_NAME = "tag";
    private static final String SEARCH = "search";
    private static final String SORT = "createDate_asc";
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final String CREATE_DATE = "createDate";
    private static final Long COUNT = 5L;

    @Mock
    private CertificateRepository mockCertificateRepository;
    @Mock
    private TagService mockTagService;
    @Mock
    private CertificateSpecification mockCertificateSpecification;

    @Spy
    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Test
    public void testGetCertificateById() {
        Certificate expected = new Certificate();
        expected.setId(CERTIFICATE_ID);

        when(mockCertificateRepository.getEntityById(false, CERTIFICATE_ID)).thenReturn(Optional.of(expected));

        Certificate actual = certificateService.getCertificateById(CERTIFICATE_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificateById_CertificateNotFoundException() {
        when(mockCertificateRepository.getEntityById(false, CERTIFICATE_ID)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateService.getCertificateById(CERTIFICATE_ID);
        });
    }

    @Test
    public void testCreateCertificate() {
        Certificate expected = new Certificate();

        when(mockCertificateRepository.save(expected)).thenReturn(expected);

        Certificate actual = certificateService.createCertificate(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateCertificate_CertificateDuplicateException() {
        Certificate expected = new Certificate();
        when(mockCertificateRepository.save(expected)).thenThrow(new CertificateDuplicateException());

        assertThrows(CertificateDuplicateException.class, () -> {
            certificateService.createCertificate(expected);
        });
    }

    @Test
    public void testGetCertificates_WithoutParams_WithoutPagination() {
        List<Certificate> expected = new ArrayList<>();
        CertificateSpecification certificateSpecification = new CertificateSpecification();
        Specification<Certificate> specification = certificateSpecification.filter(null, null);
        Sort sort = Sort.unsorted();

        doReturn(specification).when(mockCertificateSpecification).filter(null, null);
        when(mockCertificateRepository.findAll(specification, sort)).thenReturn(expected);

        List<Certificate> actual = certificateService.getCertificates(null, null, null, null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithParams_WithoutPagination() {
        List<Certificate> expected = new ArrayList<>();
        CertificateSpecification certificateSpecification = new CertificateSpecification();
        Specification<Certificate> specification = certificateSpecification.filter(SEARCH, TAG_NAME);
        Sort sort = Sort.by(CREATE_DATE).ascending();

        doReturn(specification).when(mockCertificateSpecification).filter(SEARCH, TAG_NAME);
        when(mockCertificateRepository.findAll(specification, sort)).thenReturn(expected);

        List<Certificate> actual = certificateService.getCertificates(TAG_NAME, SEARCH, SORT, null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithParams_WithPagination() {
        Page<Certificate> expected = Page.empty();
        CertificateSpecification certificateSpecification = new CertificateSpecification();
        Specification<Certificate> specification = certificateSpecification.filter(SEARCH, TAG_NAME);
        Sort sort = Sort.by(CREATE_DATE).ascending();

        doReturn(specification).when(mockCertificateSpecification).filter(SEARCH, TAG_NAME);
        when(mockCertificateRepository.findAll(specification, PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE, sort))).thenReturn(expected);
        when(mockCertificateRepository.count(specification)).thenReturn(COUNT);
        List<Certificate> actual = certificateService.getCertificates(TAG_NAME, SEARCH, SORT, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected.getContent(), actual);
    }

    @Test
    public void testGetCertificates_WithNullPageNumber_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, null, PAGE_SIZE);
        });
    }

    @Test
    public void testGetCertificates_WithNullPageSize_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, PAGE_NUMBER, null);
        });
    }

    @Test
    public void testGetCertificatesByTags() {
        List<String> tagNames = new ArrayList<>();
        Page<Certificate> expected = Page.empty();
        CertificateSpecification certificateSpecification = new CertificateSpecification();
        Specification<Certificate> specification = certificateSpecification.tagNames(tagNames);

        doReturn(specification).when(mockCertificateSpecification).tagNames(tagNames);
        when(mockCertificateRepository.findAll(specification, PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(expected);
        when(mockCertificateRepository.count(specification)).thenReturn(COUNT);

        List<Certificate> actual = certificateService.getCertificatesByTags(tagNames, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected.getContent(), actual);
    }

    @Test
    public void testUpdateCertificate() {
        Certificate updateCertificate = new Certificate();
        updateCertificate.setName(CERTIFICATE_NAME);

        Certificate expected = new Certificate();
        expected.setId(CERTIFICATE_ID);

        doReturn(Optional.of(expected)).when(mockCertificateRepository).getEntityById(false, CERTIFICATE_ID);
        when(mockTagService.updateTags(updateCertificate.getTags())).thenReturn(updateCertificate.getTags());
        when(mockCertificateRepository.save(expected)).thenReturn((expected));

        Certificate actual = certificateService.updateCertificate(updateCertificate, CERTIFICATE_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdatePartCertificate() {
        Certificate updateCertificate = new Certificate();
        updateCertificate.setName(CERTIFICATE_NAME);

        Certificate expected = new Certificate();
        expected.setId(CERTIFICATE_ID);

        doReturn(Optional.of(expected)).when(mockCertificateRepository).getEntityById(false, CERTIFICATE_ID);
        when(mockCertificateRepository.save(expected)).thenReturn((expected));

        Certificate actual = certificateService.updatePartCertificate(updateCertificate, CERTIFICATE_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteCertificate() {
        Certificate expected = new Certificate();
        expected.setId(CERTIFICATE_ID);

        doReturn(Optional.of(expected)).when(mockCertificateRepository).getEntityById(false, CERTIFICATE_ID);
        certificateService.deleteCertificate(CERTIFICATE_ID);

        verify(mockCertificateRepository).getEntityById(false, CERTIFICATE_ID);
        verify(mockCertificateRepository).deleteEntity(true, CERTIFICATE_ID);
    }

    @Test
    public void testDeleteCertificate_CertificateNotFoundException() {
        when(mockCertificateRepository.getEntityById(false, CERTIFICATE_ID)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateService.deleteCertificate(CERTIFICATE_ID);
        });
    }
}