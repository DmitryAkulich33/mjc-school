package com.epam.esm.controller;

import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import com.epam.esm.view.CreateUserView;
import com.epam.esm.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
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
    @PreAuthorize(value = "isAuthenticated() and (hasAuthority('ROLE_ADMIN') or authentication.principal.id == #id)")
    public ResponseEntity<UserView> getUserById(@PathVariable @NotNull @Positive Long id) {
        User user = userService.getUserById(id);
        UserView userView = UserView.createForm(user);

        userView.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());

        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @JsonView(UserView.Views.V1.class)
    @PostMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserView> register(@Valid @RequestBody @JsonView(CreateUserView.Views.V1.class) CreateUserView createUserView) {
        User user = CreateUserView.createForm(createUserView);
        User createdUser = userService.createUser(user);
        UserView userView = UserView.createForm(createdUser);

        userView.add(linkTo(methodOn(UserController.class).register(createUserView)).withSelfRel());

        return new ResponseEntity<>(userView, HttpStatus.CREATED);
    }

    @JsonView(UserView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CollectionModel<UserView>> getUsers(@RequestParam(required = false) @Positive Integer pageNumber,
                                                              @RequestParam(required = false) @Positive Integer pageSize) {
        List<User> users = userService.getUsers(pageNumber, pageSize);
        List<UserView> usersView = UserView.createListForm(users);

        Link link = linkTo(methodOn(UserController.class).getUsers(pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(usersView, link), HttpStatus.OK);
    }
}
