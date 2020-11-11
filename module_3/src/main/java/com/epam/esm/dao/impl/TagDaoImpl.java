package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String FIND_BY_NAME = "SELECT t FROM tag t WHERE lock_tag=0 AND name_tag=?1";
    private static final String LOCK_BY_ID = "UPDATE tag SET lock_tag=1 WHERE id_tag=?";
    private static final String FIND_BY_ID = "SELECT t FROM tag t WHERE lock_tag=0 AND id_tag=?1";
    private static final String FIND_ALL = "SELECT t FROM tag t WHERE lock_tag=0";
    private static final String ADD_TAG_CERTIFICATE = "INSERT INTO tag_certificate " +
            "(tag_id, certificate_id) VALUES(?,?)";
    private static final String FIND_TAGS_CERTIFICATE = "SELECT id_tag, name_tag, lock_tag FROM tag " +
            "JOIN tag_certificate ON tag.id_tag=tag_certificate.tag_id WHERE certificate_id=?";
    private static final Integer LOCK = 0;

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        tag.setLock(LOCK);
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
        entityManager.createNativeQuery(LOCK_BY_ID)
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public Tag getTagById(Long id) {
        return entityManager.createQuery(FIND_BY_ID, Tag.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Override
    public Tag getTagByName(String name) {
        return entityManager.createQuery(FIND_BY_NAME, Tag.class)
                .setParameter(1, name)
                .getSingleResult();
    }

    @Override
    public List<Tag> getTags() {
        return entityManager.createQuery(FIND_ALL, Tag.class).getResultList();
    }

    @Override
    public List<Tag> getCertificateTags(Long idCertificate) {
        Query tagQuery = entityManager.createNativeQuery(FIND_TAGS_CERTIFICATE, Tag.class);
        tagQuery.setParameter(1, idCertificate);

        return tagQuery.getResultList();
    }
}
