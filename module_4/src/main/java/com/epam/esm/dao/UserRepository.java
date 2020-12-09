package com.epam.esm.dao;

import com.epam.esm.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ParentEntityRepository<User> {
    Optional<User> findByLogin(String login);

    @Query("select o.user from orders o join o.user u group by o.user order by sum(o.total) desc")
    List<User> getUserWithTheLargeSumOrders();
}
