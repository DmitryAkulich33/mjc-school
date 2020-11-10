package com.epam.esm.controller;

import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import com.epam.esm.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @JsonView(UserView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserView> getTagById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserView userView = UserView.createForm(user);

        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @JsonView(UserView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserView>> getAllTags() {
        List<User> users = userService.getAllUsers();
        List<UserView> usersView = UserView.createListForm(users);

        return new ResponseEntity<>(usersView, HttpStatus.OK);
    }
}
