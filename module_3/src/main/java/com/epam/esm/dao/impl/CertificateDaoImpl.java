package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.builder.CertificateQueryBuilder;
import com.epam.esm.domain.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CertificateQueryBuilder certificateQueryBuilder;

    private static final String LOCK_BY_ID = "UPDATE certificate SET lock_certificate=1 WHERE id_certificate=?";
    private static final String FIND_BY_ID = "SELECT c FROM certificate c WHERE lock_certificate=0 AND id_certificate=?1";
    private static final String UPDATE_CERTIFICATE = "UPDATE certificate SET name_certificate=?, description=?, " +
            "price=?, update_date=?, duration=? WHERE id_certificate=?";
    private static final Integer LOCK = 0;

    @Autowired
    public CertificateDaoImpl(EntityManager entityManager, CertificateQueryBuilder certificateQueryBuilder) {
        this.entityManager = entityManager;
        this.certificateQueryBuilder = certificateQueryBuilder;
    }

    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        certificate.setLock(LOCK);
        certificate.setCreateDate(creationDate);
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public void deleteCertificate(Long id) {
        entityManager.createNativeQuery(LOCK_BY_ID)
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public Certificate getCertificateById(Long id) {
        return entityManager.createQuery(FIND_BY_ID, Certificate.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate) {
        Long id = certificate.getId();
        String name = certificate.getName();
        String description = certificate.getDescription();
        double price = certificate.getPrice();
        LocalDateTime updateDate = LocalDateTime.now();
        int duration = certificate.getDuration();
        entityManager.createNativeQuery(UPDATE_CERTIFICATE, Certificate.class)
                .setParameter(1, name)
                .setParameter(2, description)
                .setParameter(3, price)
                .setParameter(4, updateDate)
                .setParameter(5, duration)
                .setParameter(6, id);

        certificate.setLastUpdateDate(updateDate);
        return certificate;
    }

    @Override
    public List<Certificate> getCertificates(String name, String search, String sort) {
        String query = certificateQueryBuilder.buildCertificatesQuery(name, search, sort);
        Query certificatesQuery = entityManager.createNativeQuery(query, Certificate.class);
        List<Certificate> certificates = certificatesQuery.getResultList();
        return certificates;
    }
}
