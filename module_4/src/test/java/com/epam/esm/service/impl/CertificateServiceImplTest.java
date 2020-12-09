package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateDaoException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {
//    private static final Long CERTIFICATE_ID = 1L;
//    private static final String CERTIFICATE_NAME = "Certificate for one purchase";
//    private static final String TAG_NAME = "tag";
//    private static final String SEARCH = "search";
//    private static final String SORT = "createDate_asc";
//    private static final Integer PAGE_NUMBER = 1;
//    private static final Integer PAGE_SIZE = 10;
//    private static final Integer OFFSET = 0;
//    private static final Boolean SORT_ASC = true;
//    private static final String CREATE_DATE = "createDate";
//
//    @Mock
//    private CertificateDao mockCertificateDao;
//    @Mock
//    private TagService mockTagService;
//
//    @Spy
//    @InjectMocks
//    private CertificateServiceImpl certificateService;
//
//    @Test
//    public void testGetCertificateById() {
//        Certificate expected = new Certificate();
//        expected.setId(CERTIFICATE_ID);
//
//        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenReturn(Optional.of(expected));
//
//        Certificate actual = certificateService.getCertificateById(CERTIFICATE_ID);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificateById_CertificateNotFoundException() {
//        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenReturn(Optional.empty());
//
//        assertThrows(CertificateNotFoundException.class, () -> {
//            certificateService.getCertificateById(CERTIFICATE_ID);
//        });
//    }
//
//    @Test
//    public void testGetCertificateById_CertificateDaoException() {
//        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.getCertificateById(CERTIFICATE_ID);
//        });
//    }
//
//    @Test
//    public void testCreateCertificate() {
//        Certificate expected = new Certificate();
//
//        when(mockCertificateDao.createCertificate(expected)).thenReturn(expected);
//
//        Certificate actual = certificateService.createCertificate(expected);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testCreateCertificate_CertificateDaoException() {
//        Certificate expected = new Certificate();
//        when(mockCertificateDao.createCertificate(expected)).thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.createCertificate(expected);
//        });
//    }
//
//    @Test
//    public void testCreateCertificate_CertificateDuplicateException() {
//        Certificate expected = new Certificate();
//        when(mockCertificateDao.createCertificate(expected)).thenThrow(new CertificateDuplicateException());
//
//        assertThrows(CertificateDuplicateException.class, () -> {
//            certificateService.createCertificate(expected);
//        });
//    }
//
//    @Test
//    public void testGetCertificates_WithoutParams_WithoutPagination() {
//        List<Certificate> expected = new ArrayList<>();
//
//        when(mockCertificateDao.getCertificates(null, null, null, null)).thenReturn(expected);
//
//        List<Certificate> actual = certificateService.getCertificates(null, null, null, null, null);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_WithParams_WithoutPagination() {
//        List<Certificate> expected = new ArrayList<>();
//
//        when(mockCertificateDao.getCertificates(TAG_NAME, SEARCH, SORT_ASC, CREATE_DATE)).thenReturn(expected);
//
//        List<Certificate> actual = certificateService.getCertificates(TAG_NAME, SEARCH, SORT, null, null);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_WithParams_WithPagination() {
//        List<Certificate> expected = new ArrayList<>();
//
//        when(mockCertificateDao.getCertificates(TAG_NAME, SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE)).thenReturn(expected);
//
//        List<Certificate> actual = certificateService.getCertificates(TAG_NAME, SEARCH, SORT, PAGE_NUMBER, PAGE_SIZE);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_WithPagination_CertificateDaoException() {
//        when(mockCertificateDao.getCertificates(TAG_NAME, SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE))
//                .thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetCertificates_WithPagination_WrongEnteredDataException() {
//        when(mockCertificateDao.getCertificates(TAG_NAME, SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE))
//                .thenThrow(new WrongEnteredDataException());
//
//        assertThrows(WrongEnteredDataException.class, () -> {
//            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetCertificates_WithoutPagination_CertificateDaoException() {
//        when(mockCertificateDao.getCertificates(TAG_NAME, SEARCH, SORT_ASC, CREATE_DATE))
//                .thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, null, null);
//        });
//    }
//
//    @Test
//    public void testGetCertificates_WithNullPageNumber_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, null, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetCertificates_WithNullPageSize_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            certificateService.getCertificates(TAG_NAME, SEARCH, SORT, PAGE_NUMBER, null);
//        });
//    }
//
//    @Test
//    public void testGetCertificatesByTags() {
//        List<String> tagNames = new ArrayList<>();
//        List<Certificate> expected = new ArrayList<>();
//
//        when(mockCertificateDao.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE)).thenReturn(expected);
//
//        List<Certificate> actual = certificateService.getCertificatesByTags(tagNames, PAGE_NUMBER, PAGE_SIZE);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificatesByTags_CertificateDaoException() {
//        List<String> tagNames = new ArrayList<>();
//        when(mockCertificateDao.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE)).thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.getCertificatesByTags(tagNames, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetCertificatesByTags_WrongEnteredDataException() {
//        List<String> tagNames = new ArrayList<>();
//        when(mockCertificateDao.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE)).thenThrow(new WrongEnteredDataException());
//
//        assertThrows(WrongEnteredDataException.class, () -> {
//            certificateService.getCertificatesByTags(tagNames, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testUpdateCertificate() {
//        Certificate updateCertificate = new Certificate();
//        updateCertificate.setName(CERTIFICATE_NAME);
//
//        Certificate expected = new Certificate();
//        expected.setId(CERTIFICATE_ID);
//
//        doReturn(Optional.of(expected)).when(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
//        when(mockTagService.updateTags(updateCertificate.getTags())).thenReturn(updateCertificate.getTags());
//        when(mockCertificateDao.updateCertificate(expected)).thenReturn((expected));
//
//        Certificate actual = certificateService.updateCertificate(updateCertificate, CERTIFICATE_ID);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testUpdateCertificate_CertificateDaoException() {
//        Certificate updateCertificate = new Certificate();
//        updateCertificate.setName(CERTIFICATE_NAME);
//
//        Certificate expected = new Certificate();
//        expected.setId(CERTIFICATE_ID);
//
//        doReturn(Optional.of(expected)).when(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
//        when(mockTagService.updateTags(updateCertificate.getTags())).thenReturn(updateCertificate.getTags());
//        when(mockCertificateDao.updateCertificate(expected)).thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.updateCertificate(updateCertificate, CERTIFICATE_ID);
//        });
//    }
//
//    @Test
//    public void testUpdatePartCertificate() {
//        Certificate updateCertificate = new Certificate();
//        updateCertificate.setName(CERTIFICATE_NAME);
//
//        Certificate expected = new Certificate();
//        expected.setId(CERTIFICATE_ID);
//
//        doReturn(Optional.of(expected)).when(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
//        when(mockCertificateDao.updateCertificate(expected)).thenReturn((expected));
//
//        Certificate actual = certificateService.updatePartCertificate(updateCertificate, CERTIFICATE_ID);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testUpdatePartCertificate_CertificateDaoException() {
//        Certificate updateCertificate = new Certificate();
//
//        Certificate expected = new Certificate();
//        expected.setId(CERTIFICATE_ID);
//
//        doReturn(Optional.of(expected)).when(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
//        when(mockCertificateDao.updateCertificate(expected)).thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.updatePartCertificate(updateCertificate, CERTIFICATE_ID);
//        });
//    }
//
//    @Test
//    public void testDeleteCertificate() {
//        Certificate expected = new Certificate();
//        expected.setId(CERTIFICATE_ID);
//
//        doReturn(Optional.of(expected)).when(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
//        certificateService.deleteCertificate(CERTIFICATE_ID);
//
//        verify(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
//        verify(mockCertificateDao).deleteCertificate(CERTIFICATE_ID);
//    }
//
//    @Test
//    public void testDeleteCertificate_CertificateNotFoundException() {
//        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenReturn(Optional.empty());
//
//        assertThrows(CertificateNotFoundException.class, () -> {
//            certificateService.deleteCertificate(CERTIFICATE_ID);
//        });
//    }
//
//    @Test
//    public void testDeleteCertificate_CertificateDaoException() {
//        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenThrow(new CertificateDaoException());
//
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateService.deleteCertificate(CERTIFICATE_ID);
//        });
//    }
}