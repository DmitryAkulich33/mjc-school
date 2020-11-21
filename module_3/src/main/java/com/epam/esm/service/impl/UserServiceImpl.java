package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OffsetCalculator;
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
    private final OffsetCalculator offsetCalculator;
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator, PaginationValidator paginationValidator, OffsetCalculator offsetCalculator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
        this.offsetCalculator = offsetCalculator;
    }

    @Override
    public User getUserById(Long idUser) {
        log.debug(String.format("Service: search user by id %d", idUser));
        userValidator.validateUserId(idUser);
        Optional<User> user = userDao.getUserById(idUser);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("message.wrong_user_id");
        }
    }

    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        paginationValidator.validatePagination(pageNumber, pageSize);
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        List<User> users = userDao.getUsers(offset, pageSize);
        if(users.isEmpty()){
            throw new WrongEnteredDataException("message.invalid_entered_data");
        } else {
            return users;
        }
    }
}