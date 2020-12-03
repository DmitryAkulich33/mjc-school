package com.epam.esm.service.impl;

import com.epam.esm.domain.User;
import com.epam.esm.exceptions.AuthenticationDataException;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.AuthenticationService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(JwtTokenProvider jwtTokenProvider, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String login(String username, String password) {
        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userService.getUserByLogin(username);

            return jwtTokenProvider.createToken(username, user.getRole());
        } catch (AuthenticationException e) {
            throw new AuthenticationDataException("Invalid username or password");
        }
    }
}
