package com.epam.esm.service;

import com.epam.esm.domain.Certificate;

import java.util.List;

public interface CertificateService {
    Certificate createCertificate(Certificate certificate);

    Certificate updatePartCertificate(Certificate certificate, Long idCertificate);

    Certificate updateCertificate(Certificate certificate, Long idCertificate);

    void deleteCertificate(Long idCertificate);

    Certificate getCertificateById(Long idCertificate);

    List<Certificate> getCertificates(String name, String search, String sort, Integer pageNumber, Integer pageSize);

    List<Certificate> getCertificatesByTags(List<String> tagNames, Integer pageNumber, Integer pageSize);

    List<Certificate> createCertificates(List<Certificate> certificates);
}
