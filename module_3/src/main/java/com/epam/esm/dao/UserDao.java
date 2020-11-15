package com.epam.esm.dao;

import com.epam.esm.domain.User;

import java.util.List;

public interface UserDao {
    User getUserById(Long idUser);

    List<User> getUsers(Integer offset, Integer pageSize);
}
