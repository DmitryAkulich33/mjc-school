package com.epam.esm.service;

import com.epam.esm.domain.User;

import java.util.List;

public interface UserService {
    User getUserById(Long idUser);

    User getUserByLogin(String login);

    List<User> getUsers(Integer pageNumber, Integer pageSize);

    List<User> createUsers(List<User> users);
}
