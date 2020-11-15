package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PrePersist;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @PrePersist
    @Override
    public Tag createTag(Tag tag) {
        try {
            entityManager.persist(tag);
        } catch (IllegalArgumentException e) {
            throw new TagDaoException("message.wrong_data", e);
        } catch (PersistenceException e) {
            throw new TagDuplicateException("message.tag.exists", e);
        }
        return tag;
    }

    @Override
    public void deleteTag(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Tag> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Tag.class);
        Root<Tag> root = criteriaUpdate.from(Tag.class);
        criteriaUpdate.set(Tag_.lock, LOCK_VALUE_1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(Tag_.id), id));
        try {
            entityManager.createQuery(criteriaUpdate).executeUpdate();
        } catch (IllegalArgumentException e) {
            throw new TagDaoException("message.wrong_data", e);
        } catch (PersistenceException e) {
            throw new TagNotFoundException("message.wrong_tag_id", e);
        }
    }

    @Override
    public Tag getTagById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.id), id),
                criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));

        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (IllegalArgumentException e) {
            throw new TagDaoException("message.wrong_data", e);
        } catch (PersistenceException e) {
            throw new TagNotFoundException("message.wrong_tag_id", e);
        }
    }

    @Override
    public Tag getTagByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.name), name),
                criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (IllegalArgumentException e) {
            throw new TagDaoException("message.wrong_data", e);
        } catch (PersistenceException e) {
            throw new TagNotFoundException("message.wrong_tag_name", e);
        }
    }

    @Override
    public List<Tag> getTags(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            throw new TagDaoException("message.wrong_data", e);
        }
    }

    @Override
    public List<Tag> getTags() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        try {
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (IllegalArgumentException e) {
            throw new TagDaoException("message.wrong_data", e);
        }
    }
}
