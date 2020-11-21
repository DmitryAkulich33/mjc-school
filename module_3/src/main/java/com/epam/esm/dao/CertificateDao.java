package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {
    Certificate createCertificate(Certificate certificate);

    void deleteCertificate(Long idCertificate);

    Optional<Certificate> getCertificateById(Long idCertificate);

    Certificate updateCertificate(Certificate certificate);

    List<Certificate> getCertificates(String name, String search, Boolean sortAsc, String sortField, Integer offset, Integer pageSize);

    List<Certificate> getCertificates(String name, String search, Boolean sortAsc, String sortField);

    List<Certificate> getCertificatesByTags(List<String> tagNames, Integer offset, Integer pageSize);

    List<Certificate> getCertificatesByTags(List<String> tagNames);

}
