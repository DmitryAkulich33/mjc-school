package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getUserById(Long idUser) {
        TypedQuery<User> userQuery = entityManager.createNamedQuery(User.QueryNames.FIND_BY_ID, User.class);
        userQuery.setParameter("idUser", idUser);

        return userQuery.getSingleResult();
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> userQuery = entityManager.createNamedQuery(User.QueryNames.FIND_ALL, User.class);

        return userQuery.getResultList();
    }
}
