package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.domain.User_;
import com.epam.esm.exceptions.UserDaoException;
import com.epam.esm.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final Integer LOCK_VALUE_0 = 0;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getUserById(Long idUser) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.id), idUser));

        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (IllegalArgumentException e) {
            throw new UserDaoException("message.wrong_data", e);
        } catch (PersistenceException e) {
            throw new UserNotFoundException("message.wrong_user_id", e);
        }
    }

    @Override
    public List<User> getUsers(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
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
}
