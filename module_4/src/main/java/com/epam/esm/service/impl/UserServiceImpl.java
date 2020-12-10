package com.epam.esm.service.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long idUser) {
        log.debug(String.format("Service: search user by id %d", idUser));
        Optional<User> optionalUser = userRepository.getEntityById(false, idUser);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("message.wrong_user_id"));
    }

    @Override
    public User getUserByLogin(String login) {
        log.debug(String.format("Service: search user by login %s", login));
        Optional<User> optionalUser = userRepository.findByLogin(login);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("message.wrong_user_id")); // message
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        if (pageNumber != null && pageSize != null) {
            checkPagination(pageNumber, pageSize);
            return userRepository.getEntities(false, PageRequest.of(pageNumber - 1, pageSize));
        } else if (pageNumber == null && pageSize == null) {
            return userRepository.getEntities(false);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private void checkPagination(Integer pageNumber, Integer pageSize) {
        long countFromDb = userRepository.count();
        long countFromRequest = (pageNumber - 1) * pageSize;
        if (countFromDb <= countFromRequest) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Transactional
    @Override
    public List<User> createUsers(List<User> users) {
        return (List<User>) userRepository.saveAll(users);
    }
}