package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.List;

public interface CertificateDao {
    Certificate createCertificate(Certificate certificate);

    void deleteCertificate(Long idCertificate);

    Certificate getCertificateById(Long idCertificate);

    Certificate updateCertificate(Certificate certificate);

    List<Certificate> getCertificates(String name, String search, String sort);
}
