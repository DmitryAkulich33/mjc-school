package com.epam.esm.util.generator;

import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserGenerate {
    private static final int COUNT_LINES = 700;
    private final UserService userService;

    @Autowired
    public UserGenerate(UserService userService) {
        this.userService = userService;
    }

    private List<String> getWordsToCreate(int countUsers, List<String> allWords) {
        List<String> userName = IntStream.range(0, countUsers).map(i -> RandomUtils.nextInt(1, allWords.size())).mapToObj(random -> allWords.get(random - 1)).collect(Collectors.toList());
        return userName;
    }

    public List<User> generateUsers(int countUsers, List<String> names, List<String> surnames) {
        List<String> userNames = getWordsToCreate(countUsers, names);
        List<String> userSurnames = getWordsToCreate(COUNT_LINES, surnames);
        List<User> users = new ArrayList<>();
        IntStream.range(0, countUsers).map(i -> RandomUtils.nextInt(1, countUsers - 1)).forEach(randomName -> {
            int randomSurname = RandomUtils.nextInt(1, COUNT_LINES - 1);
            User user = new User();
            user.setName(userNames.get(randomName));
            user.setSurname(userSurnames.get(randomSurname));
            users.add(user);
        });
        return userService.createUsers(users);
    }
}
