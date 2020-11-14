package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;
    private static final String ADD_TAG_CERTIFICATE = "INSERT INTO tag_certificate " +
            "(tag_id, certificate_id) VALUES(?,?)";

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        tag.setLock(LOCK_VALUE_0);
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void createTagCertificate(Long idTag, Long idCertificate) {
        entityManager.createNativeQuery(ADD_TAG_CERTIFICATE)
                .setParameter(1, idTag)
                .setParameter(2, idCertificate)
                .executeUpdate();
    }

    @Override
    public void deleteTag(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Tag> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Tag.class);
        Root<Tag> root = criteriaUpdate.from(Tag.class);
        criteriaUpdate.set(Tag_.lock, LOCK_VALUE_1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(Tag_.id), id));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public Tag getTagById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.id), id),
                criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        TypedQuery<Tag> typed = entityManager.createQuery(criteriaQuery);

        return typed.getSingleResult();
    }

    @Override
    public Tag getTagByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.name), name),
                criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        TypedQuery<Tag> typed = entityManager.createQuery(criteriaQuery);

        return typed.getSingleResult();
    }

    @Override
    public List<Tag> getTags() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        TypedQuery<Tag> typed = entityManager.createQuery(criteriaQuery);

        return typed.getResultList();
    }

    @Override
    public List<Tag> getCertificateTags(Long idCertificate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        Join<Tag, Certificate> join = root.join("certificates", JoinType.INNER);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0),
                criteriaBuilder.equal(join.get(Tag_.ID), idCertificate));
        TypedQuery<Tag> typed = entityManager.createQuery(criteriaQuery);

        return typed.getResultList();
    }
}
