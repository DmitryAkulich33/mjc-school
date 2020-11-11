package com.epam.esm.util;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateValidatorException;
import org.springframework.stereotype.Component;

import java.util.List;

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
        validateCertificateTags(certificate.getTags());
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

    private void validateCertificateTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new CertificateValidatorException("message.invalid_certificate_tags");
        }
    }
}
