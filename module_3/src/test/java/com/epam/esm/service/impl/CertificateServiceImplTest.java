package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateDaoException;
import com.epam.esm.exceptions.CertificateDuplicateException;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.CertificateValidatorException;
import com.epam.esm.util.CertificateValidator;
import com.epam.esm.util.OffsetCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {
    private static final Long CERTIFICATE_ID = 1L;
    private static final String TAG_NAME = "tag";
    private static final String CERTIFICATE_SEARCH = "search";
    private static final String CERTIFICATE_SORT = "createDate_asc";
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Integer OFFSET = 0;
    private static final Boolean SORT_ASC = true;
    private static final String CREATE_DATE = "createDate";

    @Mock
    private List<Certificate> mockCertificates;
    @Mock
    private TagDao mockTagDao;
    @Mock
    private CertificateDao mockCertificateDao;
    @Mock
    private CertificateValidator mockCertificateValidator;
    @Mock
    private OffsetCalculator mockOffsetCalculator;
    @Mock
    private Certificate mockCertificate;
    @Mock
    private List<String> tagNames;


    @InjectMocks
    private CertificateServiceImpl certificateServiceImpl;

    @Test
    public void testDeleteCertificateById() {
        certificateServiceImpl.deleteCertificate(CERTIFICATE_ID);

        verify(mockCertificateValidator).validateCertificateId(CERTIFICATE_ID);
        verify(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
        verify(mockCertificateDao).deleteCertificate(CERTIFICATE_ID);
    }

    @Test
    public void testDeleteCertificateById_CertificateDaoException() {
        doThrow(new CertificateDaoException()).when(mockCertificateDao).deleteCertificate(CERTIFICATE_ID);

        assertThrows(CertificateDaoException.class, () -> {
            certificateServiceImpl.deleteCertificate(CERTIFICATE_ID);
        });
    }

    @Test
    public void testDeleteCertificateById_CertificateValidatorException() {
        doThrow(new CertificateValidatorException()).when(mockCertificateDao).deleteCertificate(CERTIFICATE_ID);

        assertThrows(CertificateValidatorException.class, () -> {
            certificateServiceImpl.deleteCertificate(CERTIFICATE_ID);
        });
    }

    @Test
    public void testDeleteCertificateById_CertificateNotFoundException() {
        doThrow(new CertificateNotFoundException()).when(mockCertificateDao).deleteCertificate(CERTIFICATE_ID);

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateServiceImpl.deleteCertificate(CERTIFICATE_ID);
        });
    }

    @Test
    public void testGetCertificateById() {
        Certificate expected = mock(Certificate.class);

        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenReturn(expected);

        Certificate actual = certificateServiceImpl.getCertificateById(CERTIFICATE_ID);

        assertEquals(expected, actual);
        verify(mockCertificateValidator).validateCertificateId(CERTIFICATE_ID);
        verify(mockCertificateDao).getCertificateById(CERTIFICATE_ID);
    }

    @Test
    public void testGetCertificateById_CertificateNotFoundException() {
        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenThrow(new CertificateNotFoundException());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateServiceImpl.getCertificateById(CERTIFICATE_ID);
        });
    }

    @Test
    public void testGetCertificateById_CertificateValidatorException() {
        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenThrow(new CertificateValidatorException());

        assertThrows(CertificateValidatorException.class, () -> {
            certificateServiceImpl.getCertificateById(CERTIFICATE_ID);
        });
    }

    @Test
    public void testGetCertificateById_CertificateDaoException() {
        when(mockCertificateDao.getCertificateById(CERTIFICATE_ID)).thenThrow(new CertificateDaoException());

        assertThrows(CertificateDaoException.class, () -> {
            certificateServiceImpl.getCertificateById(CERTIFICATE_ID);
        });
    }

    @Test
    public void testGetCertificates() {
        when(mockCertificateDao.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE)).thenReturn(mockCertificates);

        List<Certificate> actual = certificateServiceImpl.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(mockCertificates, actual);
        verify(mockCertificateDao).getCertificates(TAG_NAME, CERTIFICATE_SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE);
        verify(mockOffsetCalculator).calculateOffset(PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    public void testGetCertificates_CertificateNotFoundException() {
        when(mockCertificateDao.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE)).thenThrow(new CertificateNotFoundException());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateServiceImpl.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT, OFFSET, PAGE_SIZE);
        });
    }

    @Test
    public void testGetCertificates_CertificateDaoException() {
        when(mockCertificateDao.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, SORT_ASC, CREATE_DATE, OFFSET, PAGE_SIZE)).thenThrow(new CertificateDaoException());

        assertThrows(CertificateDaoException.class, () -> {
            certificateServiceImpl.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT, OFFSET, PAGE_SIZE);
        });
    }

    @Test
    public void testCreateCertificate() {
        Certificate expected = mock(Certificate.class);

        when(mockCertificateDao.createCertificate(expected)).thenReturn(expected);

        Certificate actual = certificateServiceImpl.createCertificate(expected);

        assertEquals(expected, actual);
        verify(mockCertificateValidator).validateCertificate(expected);
    }

    @Test
    public void testCreateCertificate_CertificateValidatorException() {
        when(mockCertificateDao.createCertificate(mockCertificate)).thenThrow(new CertificateValidatorException());

        assertThrows(CertificateValidatorException.class, () -> {
            certificateServiceImpl.createCertificate(mockCertificate);
        });
    }

    @Test
    public void testCreateCertificate_CertificateDaoException() {
        when(mockCertificateDao.createCertificate(mockCertificate)).thenThrow(new CertificateDaoException());

        assertThrows(CertificateDaoException.class, () -> {
            certificateServiceImpl.createCertificate(mockCertificate);
        });
    }

    @Test
    public void testCreateCertificate_CertificateDuplicateException() {
        when(mockCertificateDao.createCertificate(mockCertificate)).thenThrow(new CertificateDuplicateException());

        assertThrows(CertificateDuplicateException.class, () -> {
            certificateServiceImpl.createCertificate(mockCertificate);
        });
    }

    @Test
    public void testGetCertificatesByTags() {
        when(mockCertificateDao.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE)).thenReturn(mockCertificates);

        List<Certificate> actual = certificateServiceImpl.getCertificatesByTags(tagNames, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(mockCertificates, actual);
        verify(mockCertificateDao).getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE);
        verify(mockOffsetCalculator).calculateOffset(PAGE_NUMBER, PAGE_SIZE);

    }

    @Test
    public void testGetCertificatesByTags_CertificateDaoException() {
        when(mockCertificateDao.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE)).thenThrow(new CertificateDaoException());

        assertThrows(CertificateDaoException.class, () -> {
            certificateServiceImpl.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE);
        });
    }

    @Test
    public void testGetCertificatesByTags_CertificateNotFoundException() {
        when(mockCertificateDao.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE)).thenThrow(new CertificateNotFoundException());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateServiceImpl.getCertificatesByTags(tagNames, OFFSET, PAGE_SIZE);
        });
    }
}