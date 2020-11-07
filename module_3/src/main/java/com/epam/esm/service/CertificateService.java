package com.epam.esm.service;

import com.epam.esm.domain.Certificate;

import java.util.List;

public interface CertificateService {
    Certificate createCertificate(Certificate certificate);

    Certificate updateCertificate(Certificate certificate, Long idCertificate);

    void deleteCertificate(Long idCertificate);

    Certificate getCertificateById(Long idCertificate);

    List<Certificate> getCertificates(String name, String search, String sort);
}
