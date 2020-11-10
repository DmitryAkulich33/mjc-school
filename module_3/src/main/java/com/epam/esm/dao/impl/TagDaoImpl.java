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
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private final EntityManager entityManager;

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
    public int deleteTag(Long id) {
        Query tagQuery = entityManager.createNamedQuery(Tag.QueryNames.LOCK_BY_ID);
        tagQuery.setParameter("idTag", id);

        return tagQuery.executeUpdate();
    }

    @Override
    public Tag getTagById(Long id) {
        TypedQuery<Tag> tagQuery = entityManager.createNamedQuery(Tag.QueryNames.FIND_BY_ID, Tag.class);
        tagQuery.setParameter("idTag", id);

        return tagQuery.getSingleResult();
    }

    @Override
    public Tag getTagByName(String name) {
        TypedQuery<Tag> tagQuery = entityManager.createNamedQuery(Tag.QueryNames.FIND_BY_NAME, Tag.class);
        tagQuery.setParameter("nameTag", name);

        return tagQuery.getSingleResult();
    }

    @Override
    public List<Tag> getAllTags() {
        TypedQuery<Tag> tagQuery = entityManager.createNamedQuery(Tag.QueryNames.FIND_ALL, Tag.class);

        return tagQuery.getResultList();
    }

    @Override
    public List<Tag> getCertificateTags(Long idCertificate) {
        Query tagQuery = entityManager.createNativeQuery(FIND_TAGS_CERTIFICATE, Tag.class);
        tagQuery.setParameter(1, idCertificate);
        List<Tag> tags = tagQuery.getResultList();

        return tags;
    }
}
