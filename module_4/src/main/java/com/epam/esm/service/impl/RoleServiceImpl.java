package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.exceptions.RoleDuplicateException;
import com.epam.esm.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
}
