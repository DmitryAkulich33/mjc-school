package com.epam.esm.util;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateValidatorException;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class CertificateValidator {

    public void validateCertificateId(Long id) {
        if (id == null || id <= 0) {
            throw new CertificateValidatorException("message.invalid_certificate_id");
        }
    }

    public void validateCertificateName(String name) {
        if (isBlank(name)) {
            throw new CertificateValidatorException("message.invalid_certificate_name");
        }
    }

    public void validateCertificate(Certificate certificate) {
        validateCertificateName(certificate.getName());
        validateCertificateDescription(certificate.getDescription());
        validateCertificatePrice(certificate.getPrice());
        validateCertificateDuration(certificate.getDuration());
    }

    private void validateCertificateDescription(String description) {
        if (isBlank(description)) {
            throw new CertificateValidatorException("message.invalid_certificate_description");
        }
    }

    private void validateCertificatePrice(Double price) {
        if (price == null || price <= 0) {
            throw new CertificateValidatorException("message.invalid_certificate_price");
        }
    }

    private void validateCertificateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new CertificateValidatorException("message.invalid_certificate_duration");
        }
    }
}
