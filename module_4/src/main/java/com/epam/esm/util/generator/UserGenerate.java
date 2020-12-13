package com.epam.esm.util.generator;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserGenerate {
    private static final int COUNT_LINES = 700;
    private final UserService userService;
    private final String USER = "user";
    private final String ADMIN = "admin";
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserGenerate(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private List<String> getWordsToCreate(int countUsers, List<String> allWords) {
        List<String> userName = IntStream.range(0, countUsers).map(i -> RandomUtils.nextInt(1, allWords.size())).mapToObj(random -> allWords.get(random - 1)).collect(Collectors.toList());
        return userName;
    }

    public List<User> generateUsers(int countUsers, List<String> names, List<String> surnames, List<Role> roles) {
        List<String> userNames = getWordsToCreate(countUsers, names);
        List<String> userSurnames = getWordsToCreate(COUNT_LINES, surnames);
        List<User> users = new ArrayList<>();
        users.add(getAdmin(roles));
        users.addAll(getUsers(countUsers, userNames, userSurnames, roles));

        return userService.createUsers(users);
    }

    private List<Role> getUserRoles(List<Role> roles) {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roles.get(1));
        return userRoles;
    }

    private User getAdmin(List<Role> roles) {
        User user = new User();
        user.setRoles(roles);
        user.setLogin(ADMIN);
        user.setPassword(passwordEncoder.encode(ADMIN));
        user.setName(ADMIN);
        user.setSurname(ADMIN);
        return user;
    }

    private List<User> getUsers(int countUsers, List<String> userNames, List<String> userSurnames, List<Role> roles) {
        List<User> users = new ArrayList<>();
        IntStream.range(0, countUsers).forEach(i -> {
            int randomName = RandomUtils.nextInt(1, countUsers - 1);
            int randomSurname = RandomUtils.nextInt(1, COUNT_LINES - 1);
            User user = new User();
            user.setName(userNames.get(randomName));
            user.setSurname(userSurnames.get(randomSurname));
            user.setLogin(String.format("%s%d", USER, i + 1));
            user.setPassword(passwordEncoder.encode(String.format("%s%d", USER, i + 1)));
            user.setRoles(getUserRoles(roles));
            users.add(user);
        });
        return users;
    }
}
