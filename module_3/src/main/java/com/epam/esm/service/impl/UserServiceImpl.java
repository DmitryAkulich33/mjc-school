package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Long idUser) {
        log.debug(String.format("Service: search user by id %d", idUser));
        Optional<User> optionalUser = userDao.getUserById(idUser);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("message.wrong_user_id"));
    }

    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return userDao.getUsers(offset, pageSize);
        } else if (pageNumber == null && pageSize == null) {
            return userDao.getUsers();
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    @Transactional
    @Override
    public List<User> createUsers(List<User> users) {
        return userDao.createUsers(users);
    }
}