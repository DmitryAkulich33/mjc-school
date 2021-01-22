package com.epam.esm.util.generator;

import com.epam.esm.domain.Role;
import com.epam.esm.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleGenerate {
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";
    private final RoleService roleService;

    public RoleGenerate(RoleService roleService) {
        this.roleService = roleService;
    }

    public List<Role> generateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.createRole(getAdminRole()));
        roles.add(roleService.createRole(getUserRole()));
        return roles;
    }

    private Role getAdminRole() {
        Role role = new Role();
        role.setName(ROLE_ADMIN);
        return role;
    }

    private Role getUserRole() {
        Role role = new Role();
        role.setName(ROLE_USER);
        return role;
    }
}
