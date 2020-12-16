package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.exceptions.RoleDuplicateException;
import com.epam.esm.exceptions.RoleNotFoundException;
import com.epam.esm.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private static Logger log = LogManager.getLogger(RoleServiceImpl.class);

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        log.debug("Service: creation role.");
        try {
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            throw new RoleDuplicateException("message.role.exists", e);
        }
    }

    @Override
    public Role getRoleByName(String name) {
        log.debug(String.format("Service: search role by name %s", name));
        Optional<Role> optionalRole = roleRepository.findByName(name);
        return optionalRole.orElseThrow(() -> new RoleNotFoundException("message.wrong_data"));
    }
}
