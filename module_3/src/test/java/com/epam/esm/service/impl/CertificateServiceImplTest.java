package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.CertificateDaoException;
import com.epam.esm.exceptions.CertificateValidatorException;
import com.epam.esm.util.CertificateValidator;
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
    private static final String CERTIFICATE_SORT = "sort";

    @Mock
    private List<Certificate> mockCertificates;
    @Mock
    private TagDao mockTagDao;
    @Mock
    private CertificateDao mockCertificateDao;
    @Mock
    private CertificateValidator mockCertificateValidator;

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
        verify(mockTagDao).getCertificateTags(CERTIFICATE_ID);
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
    public void testGetCertificatesWithParams() {
        when(mockCertificateDao.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT)).thenReturn(mockCertificates);

        List<Certificate> actual = certificateServiceImpl.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT);

        assertEquals(mockCertificates, actual);
        verify(mockCertificateDao).getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT);
    }

    @Test
    public void testGetCertificateWithParams_CertificateNotFoundException() {
        when(mockCertificateDao.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT)).thenThrow(new CertificateDaoException());

        assertThrows(CertificateDaoException.class, () -> {
            certificateServiceImpl.getCertificates(TAG_NAME, CERTIFICATE_SEARCH, CERTIFICATE_SORT);
        });
    }

    @Test
    public void testCreateCertificate() {
        Certificate expected = mock(Certificate.class);

        when(mockCertificateDao.createCertificate(expected)).thenReturn(expected);

        Certificate actual = certificateServiceImpl.createCertificate(expected);

        assertEquals(expected, actual);
        verify(mockCertificateValidator).validateCertificate(expected);
        verify(mockTagDao).getAllTags();
    }

    @Test
    public void testCreateCertificate_CertificateValidatorException() {
        Certificate mockCertificate = mock(Certificate.class);

        when(mockCertificateDao.createCertificate(mockCertificate)).thenThrow(new CertificateValidatorException());

        assertThrows(CertificateValidatorException.class, () -> {
            certificateServiceImpl.createCertificate(mockCertificate);
        });
    }
}