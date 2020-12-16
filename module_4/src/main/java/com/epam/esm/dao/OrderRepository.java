package com.epam.esm.dao;

import com.epam.esm.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CommonEntityRepository<Order> {
    List<Order> findByUserId(Long id, Pageable pageable);

    List<Order> findByUserId(Long id);

    long countAllByUserId(Long id);

    Optional<Order> findByUserIdAndId(Long idUser, Long idOrder);
}
