package com.epam.esm.security;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public JwtUser create(User user) {
        return new JwtUser(
                user.getLogin(),
                user.getPassword(),
                convertToGrantedAuthorities(user.getRoles())
        );
    }

    private List<GrantedAuthority> convertToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
