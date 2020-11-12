package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.builder.CertificateQueryBuilder;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CertificateQueryBuilder certificateQueryBuilder;

    private static final String LOCK = "lock";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String UPDATE_DATE = "lastUpdateDate";
    private static final String DURATION = "duration";
    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Autowired
    public CertificateDaoImpl(EntityManager entityManager, CertificateQueryBuilder certificateQueryBuilder) {
        this.entityManager = entityManager;
        this.certificateQueryBuilder = certificateQueryBuilder;
    }

    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        certificate.setLock(LOCK_VALUE_0);
        certificate.setCreateDate(creationDate);
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public void deleteCertificate(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Certificate> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Certificate.class);
        Root<Certificate> root = criteriaUpdate.from(Certificate.class);
        criteriaUpdate.set(LOCK, LOCK_VALUE_1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(ID), id));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public Certificate getCertificateById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ID), id));
        TypedQuery<Certificate> typed = entityManager.createQuery(criteriaQuery);

        return typed.getSingleResult();
    }

    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate, Long idCertificate) {
        LocalDateTime updateDate = LocalDateTime.now();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Certificate> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Certificate.class);
        Root<Certificate> root = criteriaUpdate.from(Certificate.class);
        criteriaUpdate.set(NAME, certificate.getName());
        criteriaUpdate.set(DESCRIPTION, certificate.getDescription());
        criteriaUpdate.set(PRICE, certificate.getPrice());
        criteriaUpdate.set(UPDATE_DATE, updateDate);
        criteriaUpdate.set(DURATION, certificate.getDuration());
        criteriaUpdate.where(criteriaBuilder.equal(root.get(ID), idCertificate));
        entityManager.createQuery(criteriaUpdate).executeUpdate();

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
