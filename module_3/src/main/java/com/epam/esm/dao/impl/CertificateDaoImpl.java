package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private final EntityManager entityManager;

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
    public CertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
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
    public List<Certificate> getCertificates(String name, String search, Boolean sortAsc, String sortField) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        List<Predicate> conditions = new ArrayList<>();
        System.out.println(name);
        if (name != null) {
            Join<Certificate, Tag> join = root.join("tags", JoinType.INNER);
            Predicate tagPredicate = criteriaBuilder.equal(join.get(NAME), name);
            conditions.add(tagPredicate);
        }

        if (search != null) {
            Predicate searchCondition = criteriaBuilder.or(criteriaBuilder.like(root.get(DESCRIPTION), "%" + search + "%"),
                    criteriaBuilder.like(root.get(NAME), "%" + search + "%"));
            conditions.add(searchCondition);
        }

        TypedQuery<Certificate> typed = entityManager.createQuery(getCertificateCriteriaQuery(conditions, criteriaQuery,
                root, criteriaBuilder, sortField, sortAsc));

        return typed.getResultList();
    }

    private CriteriaQuery<Certificate> getCertificateCriteriaQuery(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                                   Root<Certificate> root, CriteriaBuilder criteriaBuilder,
                                                                   String sortField, Boolean sortAsc) {
        if (conditions.isEmpty()) {
            if (sortAsc == null) {
                criteriaQuery.select(root);
            } else if (sortAsc) {
                criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get(sortField)));
            } else {
                criteriaQuery.select(root).orderBy(criteriaBuilder.desc(root.get(sortField)));
            }
        } else {
            if (sortAsc == null) {
                criteriaQuery.select(root)
                        .distinct(true)
                        .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));
            } else if (sortAsc) {
                criteriaQuery.select(root)
                        .distinct(true)
                        .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])))
                        .orderBy(criteriaBuilder.asc(root.get(sortField)));
            } else {
                criteriaQuery.select(root)
                        .distinct(true)
                        .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])))
                        .orderBy(criteriaBuilder.desc(root.get(sortField)));
            }
        }
        return criteriaQuery;
    }
}
