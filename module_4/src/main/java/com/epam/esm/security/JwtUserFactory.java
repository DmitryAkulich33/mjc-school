package com.epam.esm.security;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public JwtUser create(User user){
        return new JwtUser(
                user.getLogin(),
                user.getPassword(),
                convertToGrantedAuthorities(user.getRole())
        );
    }

    private List<GrantedAuthority> convertToGrantedAuthorities (Role role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }
}
