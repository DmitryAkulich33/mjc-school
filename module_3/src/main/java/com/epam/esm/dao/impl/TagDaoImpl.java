package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
import com.epam.esm.exceptions.TagDaoException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String GET_THE_MOST_USED_TAG = "SELECT t_tag, t_tag, lock_tag FROM tag INNER JOIN " +
            "(SELECT tag_id, count(tag_id) as max_val FROM " +
            "(SELECT tag_id FROM tag_certificate INNER JOIN " +
            "(SELECT * FROM certificate_order INNER JOIN " +
            "(SELECT o.id_order, o.id_user FROM orders o INNER JOIN " +
            "(SELECT id_user FROM orders WHERE total = " +
            "(SELECT max(total) FROM orders)) AS o_u ON o.id_user=o_u.id_user)" +
            " AS c_o ON certificate_order.order_id=c_o.id_order)" +
            " AS t_c ON tag_certificate.certificate_id=t_c.certificate_id)" +
            " AS t_v group by t_v.tag_id ORDER BY max_val DESC LIMIT 1)" +
            " AS t ON tag.id_tag=t.tag_id";

    @PersistenceContext
    private EntityManager entityManager;

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Transactional
    @PrePersist
    @Override
    public Tag createTag(Tag tag) {
        try {
            entityManager.persist(tag);
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("message.wrong_data", e);
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
            return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
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
            return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("message.wrong_data", e);
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
    public List<Tag> getTheMostUsedTag() {
       return entityManager.createQuery(GET_THE_MOST_USED_TAG, Tag.class).getResultList();
    }
}
