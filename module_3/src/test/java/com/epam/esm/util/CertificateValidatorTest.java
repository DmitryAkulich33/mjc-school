package com.epam.esm.util;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CertificateValidatorTest {
    private static final Long CORRECT_CERTIFICATE_ID = 1L;
    private static final Long WRONG_CERTIFICATE_ID = -1L;
    private static final String CORRECT_CERTIFICATE_NAME = "food";
    private static final String WRONG_CERTIFICATE_NAME = " ";
    private static final String CORRECT_CERTIFICATE_DESCRIPTION = "Free food and drink";
    private static final Double CORRECT_CERTIFICATE_PRICE = 20.0;
    private static final Double WRONG_CERTIFICATE_PRICE = 0.0;
    private static final Integer CORRECT_CERTIFICATE_DURATION = 365;

    private CertificateValidator certificateValidator;

    @BeforeEach
    public void setUp() {
        certificateValidator = new CertificateValidator();
    }

    @Test
    public void testValidateCertificateId() {
        certificateValidator.validateCertificateId(CORRECT_CERTIFICATE_ID);
    }

    @Test
    public void testValidateCertificateId_CertificateValidatorException() {
        assertThrows(CertificateValidatorException.class, () -> {
            certificateValidator.validateCertificateId(WRONG_CERTIFICATE_ID);
        });
    }

    @Test
    public void testValidateCertificateName() {
        certificateValidator.validateCertificateName(CORRECT_CERTIFICATE_NAME);
    }

    @Test
    public void testValidateCertificateName_CertificateValidatorException() {
        assertThrows(CertificateValidatorException.class, () -> {
            certificateValidator.validateCertificateName(WRONG_CERTIFICATE_NAME);
        });
    }

    @Test
    public void testValidateCertificate() {
        Certificate certificate = new Certificate();
        certificate.setName(CORRECT_CERTIFICATE_NAME);
        certificate.setDescription(CORRECT_CERTIFICATE_DESCRIPTION);
        certificate.setPrice(CORRECT_CERTIFICATE_PRICE);
        certificate.setDuration(CORRECT_CERTIFICATE_DURATION);
        certificateValidator.validateCertificate(certificate);
    }

    @Test
    public void testValidateCertificate_CertificateValidatorException() {
        Certificate certificate = new Certificate();
        certificate.setName(CORRECT_CERTIFICATE_NAME);
        certificate.setDescription(CORRECT_CERTIFICATE_DESCRIPTION);
        certificate.setPrice(WRONG_CERTIFICATE_PRICE);
        certificate.setDuration(CORRECT_CERTIFICATE_DURATION);
        assertThrows(CertificateValidatorException.class, () -> {
            certificateValidator.validateCertificate(certificate);
        });
    }
}