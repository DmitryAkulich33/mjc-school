package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Certificate_;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
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
        criteriaUpdate.set(Certificate_.lock, LOCK_VALUE_1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(Certificate_.id), id));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public Certificate getCertificateById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Certificate_.id), id),
                criteriaBuilder.equal(root.get(Certificate_.lock), LOCK_VALUE_0));
        TypedQuery<Certificate> typed = entityManager.createQuery(criteriaQuery);

        return typed.getSingleResult();
    }

    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate) {
        LocalDateTime updateDate = LocalDateTime.now();
        certificate.setLastUpdateDate(updateDate);

        return entityManager.merge(certificate);
    }

    @Override
    public List<Certificate> getCertificates(String name, String search, Boolean sortAsc, String sortField) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);

        List<Predicate> conditions = getPredicates(name, search, root, criteriaBuilder);
        TypedQuery<Certificate> typed = entityManager.createQuery(getCertificateCriteriaQuery(conditions, criteriaQuery,
                root, criteriaBuilder, sortField, sortAsc));

        return typed.getResultList();
    }

    private List<Predicate> getPredicates(String name, String search, Root<Certificate> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditions = new ArrayList<>();
        if (name != null) {
            Join<Certificate, Tag> join = root.join(Certificate_.tags, JoinType.INNER);
            Predicate tagPredicate = criteriaBuilder.equal(join.get(Tag_.name), name);
            conditions.add(tagPredicate);
        }

        if (search != null) {
            Predicate searchCondition = criteriaBuilder.or(criteriaBuilder.like(root.get(Certificate_.description), "%" + search + "%"),
                    criteriaBuilder.like(root.get(Certificate_.name), "%" + search + "%"));
            conditions.add(searchCondition);
        }

        Predicate notLock = criteriaBuilder.equal(root.get(Certificate_.lock), LOCK_VALUE_0);
        conditions.add(notLock);

        return conditions;
    }

    private CriteriaQuery<Certificate> getCertificateCriteriaQuery(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                                   Root<Certificate> root, CriteriaBuilder criteriaBuilder,
                                                                   String sortField, Boolean sortAsc) {
        return (sortAsc == null) ? selectWithoutSort(conditions, criteriaQuery, root, criteriaBuilder) :
                selectSort(conditions, criteriaQuery, root, criteriaBuilder, sortField, sortAsc);
    }

    private CriteriaQuery<Certificate> selectSort(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                  Root<Certificate> root, CriteriaBuilder criteriaBuilder,
                                                  String sortField, Boolean sortAsc) {
        return (sortAsc) ? selectSortAsc(conditions, criteriaQuery, root, criteriaBuilder, sortField) :
                selectSortDesc(conditions, criteriaQuery, root, criteriaBuilder, sortField);
    }

    private CriteriaQuery<Certificate> selectSortAsc(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                     Root<Certificate> root, CriteriaBuilder criteriaBuilder, String sortField) {
        return criteriaQuery.select(root)
                .distinct(true)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])))
                .orderBy(criteriaBuilder.asc(root.get(sortField)));
    }

    private CriteriaQuery<Certificate> selectSortDesc(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                      Root<Certificate> root, CriteriaBuilder criteriaBuilder, String sortField) {
        return criteriaQuery.select(root)
                .distinct(true)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])))
                .orderBy(criteriaBuilder.desc(root.get(sortField)));
    }

    private CriteriaQuery<Certificate> selectWithoutSort(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                         Root<Certificate> root, CriteriaBuilder criteriaBuilder) {
        return criteriaQuery.select(root)
                .distinct(true)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));
    }

    @Override
    public List<Certificate> getCertificatesByTags(List<String> tagNames) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);

        Join<Certificate, Tag> join = root.join(Certificate_.tags, JoinType.INNER);
        Expression<String> tagNamesFromDb = join.get(Tag_.name);

        criteriaQuery.select(root).distinct(true).where(tagNamesFromDb.in(tagNames),
                criteriaBuilder.equal(root.get(Certificate_.lock), LOCK_VALUE_0));

        TypedQuery<Certificate> typed = entityManager.createQuery(criteriaQuery);

        return typed.getResultList();
    }
}
