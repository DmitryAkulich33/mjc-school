package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String FIND_ALL = "SELECT o FROM orders o WHERE lock_order=0";
    private static final String FIND_BY_ID = "SELECT o FROM orders o WHERE lock_order=0 AND id_order=?1";
    private static final String FIND_ALL_BY_USER_ID = "SELECT o FROM orders o WHERE lock_order=0 AND id_user=?1";
    private static final String FIND_BY_USER_ID_AND_ORDER_ID = "SELECT o FROM orders o WHERE id_user=?1 AND id_order=?2";

    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        return entityManager.createQuery(FIND_BY_ID, Order.class)
                .setParameter(1, idOrder)
                .getSingleResult();
    }

    @Override
    public List<Order> getOrders() {
        return entityManager.createQuery(FIND_ALL, Order.class).getResultList();
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser) {
        return entityManager.createQuery(FIND_ALL_BY_USER_ID, Order.class)
                .setParameter(1, idUser)
                .getResultList();
    }

    @Override
    public Order getDataByUserId(Long idUser, Long idOrder) {
        return entityManager.createQuery(FIND_BY_USER_ID_AND_ORDER_ID, Order.class)
                .setParameter(1, idUser)
                .setParameter(2, idOrder)
                .getSingleResult();
    }
}
