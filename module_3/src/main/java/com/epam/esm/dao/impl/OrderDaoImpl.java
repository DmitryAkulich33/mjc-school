package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery(Order.QueryNames.FIND_BY_ID, Order.class);
        orderQuery.setParameter("idOrder", idOrder);

        return orderQuery.getSingleResult();
    }

    @Override
    public List<Order> getAllOrders() {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery(Order.QueryNames.FIND_ALL, Order.class);

        return orderQuery.getResultList();
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long idUser) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery(Order.QueryNames.FIND_ALL_BY_USER_ID, Order.class);
        orderQuery.setParameter("idUser", idUser);

        return orderQuery.getResultList();
    }
}
