package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String FIND_ALL = "SELECT u FROM user u WHERE lock_user=0";
    private static final String FIND_BY_ID = "SELECT u FROM user u WHERE lock_user=0 AND id_user=?1";

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getUserById(Long idUser) {
        return entityManager.createQuery(FIND_BY_ID, User.class)
                .setParameter(1, idUser)
                .getSingleResult();
    }

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery(FIND_ALL, User.class).getResultList();
    }
}
