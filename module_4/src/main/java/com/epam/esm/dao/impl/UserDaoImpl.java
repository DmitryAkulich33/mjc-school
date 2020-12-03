package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.domain.User_;
import com.epam.esm.exceptions.UserDaoException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private static final String FIND_USER_WITH_THE_LARGEST_ORDER_TOTALS =
            "select o.user from orders o join o.user u group by o.user order by sum(o.total) desc";

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private static final Integer LOCK_VALUE_0 = 0;

    @Override
    public Optional<User> getUserById(Long idUser) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.id), idUser));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.login), login));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        checkPagination(offset, criteriaBuilder);

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.lock), LOCK_VALUE_0));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
    }

    @Override
    public List<User> getUsers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.lock), LOCK_VALUE_0));

        try {
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (IllegalArgumentException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
    }

    @Transactional
    @Override
    public List<User> createUsers(List<User> users) {
        int i = 0;
        try {
            for (User user : users) {
                entityManager.persist(user);
                i++;
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
        return users;
    }

    @Override
    public User getUserWithTheLargeSumOrders() {
        Query query = entityManager.createQuery(FIND_USER_WITH_THE_LARGEST_ORDER_TOTALS);
        try {
            return (User) query.getResultList().stream().findFirst().get();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new UserDaoException("message.wrong_data", e);
        }
    }

    private void checkPagination(Integer offset, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Long count = entityManager.createQuery(countQuery.select(criteriaBuilder.count(countQuery.from(User.class)))).getSingleResult();
        if (count <= offset) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }
}
