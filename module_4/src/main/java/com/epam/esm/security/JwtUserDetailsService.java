package com.epam.esm.security;

import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final JwtUserFactory jwtUserFactory;

    @Autowired
    public JwtUserDetailsService(UserService userService, JwtUserFactory jwtUserFactory) {
        this.userService = userService;
        this.jwtUserFactory = jwtUserFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByLogin(username);
        return jwtUserFactory.create(user);
    }
}
