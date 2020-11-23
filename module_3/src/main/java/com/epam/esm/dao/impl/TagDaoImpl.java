package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String GET_THE_MOST_USED_TAG = "select tag.id_tag, tag.name_tag, tag.lock_tag FROM tag JOIN" +
            "(select tag_id,count(tag_id) as max_val FROM " +
            "(select tag_id FROM tag_certificate JOIN" +
            "(select certificate_id from certificate_order where order_id IN  " +
            "(select id_order from orders where id_user = " +
            "(select id_user from " +
            "(select id_user, sum(total) as totals from orders group by id_user order by sum(total) desc) as st where st.totals = " +
            "(select sum(total) from orders group by id_user order by sum(total) desc limit 1)))) AS t " +
            "ON tag_certificate.certificate_id=t.certificate_id) AS t_v " +
            "group by t_v.tag_id order by max_val desc limit 1) as t ON t.tag_id=tag.id_tag";

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Transactional
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
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("message.wrong_data", e);
        }
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.id), id),
                criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));

        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("message.wrong_data", e);
        }
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Tag_.name), name),
                criteriaBuilder.equal(root.get(Tag_.lock), LOCK_VALUE_0));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("message.wrong_data", e);
        }
    }

    @Override
    public List<Tag> getTags(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        checkPagination(offset, criteriaBuilder);

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

    private void checkPagination(Integer offset, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Long count = entityManager.createQuery(countQuery.select(criteriaBuilder.count(countQuery.from(Tag.class)))).getSingleResult();
        if (count <= offset) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
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

    @Override
    public Tag getTheMostUsedTag() {
        return (Tag) entityManager.createNativeQuery(GET_THE_MOST_USED_TAG, Tag.class).getSingleResult();
    }

    @Transactional
    @Override
    public List<Tag> createTags(List<Tag> tags) {
        int i = 0;
        try {
            for (Tag tag : tags) {
                entityManager.persist(tag);
                i++;
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("message.wrong_data", e);
        }
        return tags;
    }
}
