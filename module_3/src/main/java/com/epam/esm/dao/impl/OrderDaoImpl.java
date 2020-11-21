package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Order_;
import com.epam.esm.domain.User;
import com.epam.esm.domain.User_;
import com.epam.esm.exceptions.OrderDaoException;
import com.epam.esm.exceptions.OrderNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PrePersist;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    private static final Integer LOCK_VALUE_0 = 0;

    @Override
    public Optional<Order> getOrderById(Long idOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Order_.id), idOrder));

        try {
            return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new OrderDaoException("message.wrong_data", e);
        }
    }

    @Override
    public List<Order> getOrders(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Order_.lock), LOCK_VALUE_0));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            throw new OrderDaoException("message.wrong_data", e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootOrder = criteriaQuery.from(Order.class);

        Subquery<User> userSubquery = criteriaQuery.subquery(User.class);
        Root<User> userRoot = userSubquery.from(User.class);
        userSubquery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(User_.id), idUser));
        criteriaQuery.select(rootOrder).where(criteriaBuilder.equal(rootOrder.get(Order_.lock), LOCK_VALUE_0),
                criteriaBuilder.in(rootOrder.get(Order_.user)).value(userSubquery));

        try {
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (IllegalArgumentException e) {
            throw new OrderDaoException("message.wrong_data", e);
        } catch (PersistenceException e) {
            throw new OrderNotFoundException("message.wrong_user_id", e);
        }
    }

    @Override
    public Optional<Order> getOrderDataByUserId(Long idUser, Long idOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootOrder = criteriaQuery.from(Order.class);

        Subquery<User> userSubquery = criteriaQuery.subquery(User.class);
        Root<User> userRoot = userSubquery.from(User.class);
        userSubquery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(User_.id), idUser));

        criteriaQuery.select(rootOrder).where(criteriaBuilder.equal(rootOrder.get(Order_.id), idOrder),
                criteriaBuilder.in(rootOrder.get(Order_.user)).value(userSubquery));

        try {
            return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new OrderDaoException("message.wrong_data", e);
        }
    }

    @Transactional
    @PrePersist
    @Override
    public Order createOrder(Order order) {
        try {
            entityManager.persist(order);
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new OrderDaoException("message.wrong_data");
        }
        return order;
    }
}
