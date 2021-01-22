package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.exceptions.RoleDuplicateException;
import com.epam.esm.exceptions.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private static final String ROLE_NAME = "ROLE_USER";

    @Mock
    private RoleRepository mockRoleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void testCreateRole() {
        Role expected = new Role();

        when(mockRoleRepository.save(expected)).thenReturn(expected);

        Role actual = roleService.createRole(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateRole_RoleDuplicateException() {
        Role expected = new Role();
        when(mockRoleRepository.save(expected)).thenThrow(new RoleDuplicateException());

        assertThrows(RoleDuplicateException.class, () -> {
            roleService.createRole(expected);
        });
    }

    @Test
    public void testGetRoleByName() {
        Role expected = new Role();
        expected.setName(ROLE_NAME);

        when(mockRoleRepository.findByName(ROLE_NAME)).thenReturn(Optional.of(expected));

        Role actual = roleService.getRoleByName(ROLE_NAME);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRoleByName_RoleNotFoundException() {
        when(mockRoleRepository.findByName(ROLE_NAME)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            roleService.getRoleByName(ROLE_NAME);
        });
    }
}