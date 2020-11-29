package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.*;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.UserDaoException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.*;

@Repository
public class TagDaoImpl implements TagDao {
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

    @Transactional(readOnly = true)
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

    @Override
    public Optional<List<Tag>> getMostWidelyUsedTagsOfUsersWithHighestCostOfOrders() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Order> root = cq.from(Order.class);
        cq.multiselect(root.get(Order_.user), cb.sum(root.get(Order_.total)));
        cq.where(cb.equal(root.get(Order_.lock), 0));
        cq.groupBy(root.get(Order_.user));

        try {
            List<Object[]> list = entityManager.createQuery(cq).getResultList();

            Optional<List<Long>> targetUserIds = list.stream()
                    .map(objects -> new ImmutablePair<>((User) objects[0], (Double) objects[1]))
                    .collect(groupingBy(pair -> pair.right))
                    .entrySet().stream()
                    .max(comparingDouble(Map.Entry::getKey))
                    .map(doubleListEntry -> doubleListEntry.getValue().stream()
                            .map(pair -> pair.left.getId())
                            .collect(toList()));
            if (targetUserIds.isPresent()) {
                CriteriaQuery<Order> cqOrder = cb.createQuery(Order.class);
                Root<Order> rootOrder = cqOrder.from(Order.class);
                cqOrder.select(rootOrder).where(cb.and(cb.equal(rootOrder.get(Order_.lock), 0), rootOrder.get(Order_.user).get(User_.id).in(targetUserIds.get())));

                List<Order> orders = entityManager.createQuery(cqOrder).getResultList();
                return orders.stream()
                        .flatMap(order -> order.getCertificates().stream()).flatMap(certificate -> certificate.getTags().stream())
                        .collect(groupingBy(tag -> tag, counting())).entrySet().stream()
                        .collect(groupingBy(Map.Entry::getValue)).entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                                .map(Map.Entry::getKey).collect(toList()))).entrySet().stream()
                        .max(comparingLong(Map.Entry::getKey))
                        .map(Map.Entry::getValue);
            } else {
                return Optional.empty();
            }
        } catch (IllegalArgumentException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
    }
}
