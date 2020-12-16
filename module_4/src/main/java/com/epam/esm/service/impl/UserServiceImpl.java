package com.epam.esm.service.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserDuplicateException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;
    private final RoleService roleService;

    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
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

    @Transactional
    @Override
    public User createUser(User user) {
        Role role = roleService.getRoleByName(ROLE_USER);
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        user.setPassword(encode(user.getPassword()));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserDuplicateException("message.user.exists", e);
        }
    }

    private String encode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}