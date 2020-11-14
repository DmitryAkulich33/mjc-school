package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Order_;
import com.epam.esm.domain.User;
import com.epam.esm.domain.User_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final Integer LOCK_VALUE_0 = 0;

    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Order_.id), idOrder));
        TypedQuery<Order> typed = entityManager.createQuery(criteriaQuery);
        return typed.getSingleResult();
    }

    @Override
    public List<Order> getOrders() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Order_.lock), LOCK_VALUE_0));
        TypedQuery<Order> typed = entityManager.createQuery(criteriaQuery);

        return typed.getResultList();
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootOrder = criteriaQuery.from(Order.class);
        criteriaQuery.select(rootOrder).where(criteriaBuilder.equal(rootOrder.get(Order_.lock), LOCK_VALUE_0));

        Subquery<User> userSubquery = criteriaQuery.subquery(User.class);
        Root<User> userRoot = userSubquery.from(User.class);
        userSubquery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(User_.id), idUser));
        criteriaQuery.select(rootOrder).where(criteriaBuilder.in(rootOrder.get(Order_.user)).value(userSubquery));

        TypedQuery<Order> typed = entityManager.createQuery(criteriaQuery);

        return typed.getResultList();
    }

    @Override
    public Order getDataByUserId(Long idUser, Long idOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootOrder = criteriaQuery.from(Order.class);

        Subquery<User> userSubquery = criteriaQuery.subquery(User.class);
        Root<User> userRoot = userSubquery.from(User.class);
        userSubquery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(User_.id), idUser));

        criteriaQuery.select(rootOrder).where(criteriaBuilder.equal(rootOrder.get(Order_.id), idOrder),
                criteriaBuilder.in(rootOrder.get(Order_.user)).value(userSubquery));

        TypedQuery<Order> typed = entityManager.createQuery(criteriaQuery);

        return typed.getSingleResult();
    }
}
