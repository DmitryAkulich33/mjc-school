package com.epam.esm.dao;

import com.epam.esm.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(Long idUser);

    List<User> getUsers(Integer offset, Integer pageSize);

    List<User> getUsers();

    User createUser(User user);

    List<User> createUsers(List<User> users);

    User getUserWithTheLargeSumOrders();
}
