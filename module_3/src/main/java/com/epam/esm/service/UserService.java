package com.epam.esm.service;

import com.epam.esm.domain.User;

import java.util.List;

public interface UserService {
    User getUserById(Long idUser);

    List<User> getAllUsers();
}