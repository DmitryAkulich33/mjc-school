package com.epam.esm.dao.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.domain.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoleRepositoryTest {
    private static final Long ID_1 = 1L;
    private static final String ROLE_NAME = "ROLE_USER";

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole() {
        Role role = new Role();
        role.setName(ROLE_NAME);
        Role actual = roleRepository.save(role);

        assert actual.getId().equals(ID_1);
        assert actual.getName().equals(role.getName());
    }
}
