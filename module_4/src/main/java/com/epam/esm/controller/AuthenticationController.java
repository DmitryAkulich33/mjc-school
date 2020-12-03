package com.epam.esm.controller;

import com.epam.esm.service.AuthenticationService;
import com.epam.esm.view.AuthenticationRequestView;
import com.epam.esm.view.AuthenticationResponseView;
import com.epam.esm.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Validated
@RestController
@RequestMapping(value = "/api/v1/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @JsonView(AuthenticationResponseView.Views.V1.class)
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseView> login(@Valid @RequestBody @JsonView(AuthenticationRequestView.Views.V1.class) AuthenticationRequestView authenticationRequestView) {
        String username = authenticationRequestView.getLogin();
        String password = authenticationRequestView.getPassword();
        String token = authenticationService.login(username, password);

        AuthenticationResponseView view = new AuthenticationResponseView(username, token);

        return new ResponseEntity<>(view, HttpStatus.OK);
    }
}
