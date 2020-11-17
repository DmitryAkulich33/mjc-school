package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    /**
     * Dao for this server
     */
    private final UserDao userDao;

    /**
     * Validator for this service
     */
    private final UserValidator userValidator;

    /**
     * Offset's calculator for this service
     */
    private final OffsetCalculator offsetCalculator;

    /**
     * Logger for this service
     */
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    /**
     * Constructor - creating a new object
     *
     * @param userDao          dao for this service
     * @param userValidator    validator for this service
     * @param offsetCalculator offset's calculator for this service
     */
    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator, OffsetCalculator offsetCalculator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.offsetCalculator = offsetCalculator;
    }

    /**
     * Get user by id
     *
     * @param idUser user's id
     * @return user
     */
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

    /**
     * Get users
     *
     * @param pageNumber page number
     * @param pageSize   page size
     * @return list of users
     */
    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        return userDao.getUsers(offset, pageSize);
    }
}