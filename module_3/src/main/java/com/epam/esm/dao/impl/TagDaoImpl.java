package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String LOCK = "lock";
    private static final String ID = "id";
    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;
    private static final String FIND_BY_NAME = "SELECT t FROM tag t WHERE lock_tag=0 AND name_tag=?1";
    private static final String ADD_TAG_CERTIFICATE = "INSERT INTO tag_certificate " +
            "(tag_id, certificate_id) VALUES(?,?)";
    private static final String FIND_TAGS_CERTIFICATE = "SELECT id_tag, name_tag, lock_tag FROM tag " +
            "JOIN tag_certificate ON tag.id_tag=tag_certificate.tag_id WHERE certificate_id=?";


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
        criteriaUpdate.set(LOCK, LOCK_VALUE_1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(ID), id));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public Tag getTagById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ID), id));
        TypedQuery<Tag> typed = entityManager.createQuery(criteriaQuery);

        return typed.getSingleResult();
    }

    @Override
    public Tag getTagByName(String name) {
        return entityManager.createQuery(FIND_BY_NAME, Tag.class)
                .setParameter(1, name)
                .getSingleResult();
    }

    @Override
    public List<Tag> getTags() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        TypedQuery<Tag> typed = entityManager.createQuery(criteriaQuery);

        return typed.getResultList();
    }

    @Override
    public List<Tag> getCertificateTags(Long idCertificate) {
        Query tagQuery = entityManager.createNativeQuery(FIND_TAGS_CERTIFICATE, Tag.class);
        tagQuery.setParameter(1, idCertificate);

        return tagQuery.getResultList();
    }
}
