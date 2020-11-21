package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.PaginationValidator;
import com.epam.esm.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator, PaginationValidator paginationValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public User getUserById(Long idUser) {
        log.debug(String.format("Service: search user by id %d", idUser));
        userValidator.validateUserId(idUser);
        Optional<User> optionalUser = userDao.getUserById(idUser);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("message.wrong_user_id"));
    }

    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        paginationValidator.validatePagination(pageNumber, pageSize);
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return userDao.getUsers(offset, pageSize);
        } else {
            return userDao.getUsers();
        }
    }
}