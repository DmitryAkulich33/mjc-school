package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import com.epam.esm.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserValidator userValidator;

    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
    }

    @Override
    public User getUserById(Long idUser) {
        log.debug(String.format("Service: search user by id %d", idUser));
        userValidator.validateUserId(idUser);
        return userDao.getUserById(idUser);
    }

    @Override
    public List<User> getUsers() {
        log.debug("Service: search all users.");
        return userDao.getUsers();
    }
}