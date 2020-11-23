package com.epam.esm.util.generator;

import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGenerate {
    private static final int COUNT_LINES = 700;
    private final UserService userService;

    public UserGenerate(UserService userService) {
        this.userService = userService;
    }

    private List<String> getWordsToCreate(int countUsers, List<String> allWords) {
        List<String> userName = new ArrayList<>();
        for (int i = 0; i < countUsers; i++) {
            int random = RandomUtils.nextInt(1, allWords.size());
            userName.add(allWords.get(random - 1));
        }
        return userName;
    }

    public List<User> generateUsers(int countUsers, List<String> names, List<String> surnames) {
        List<String> userNames = getWordsToCreate(countUsers, names);
        List<String> userSurnames = getWordsToCreate(COUNT_LINES, surnames);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < countUsers; i++) {
            int randomName = RandomUtils.nextInt(1, countUsers - 1);
            int randomSurname = RandomUtils.nextInt(1, COUNT_LINES - 1);
            User user = new User();
            user.setName(userNames.get(randomName));
            user.setSurname(userSurnames.get(randomSurname));
            users.add(user);
        }
        return userService.createUsers(users);
    }
}
