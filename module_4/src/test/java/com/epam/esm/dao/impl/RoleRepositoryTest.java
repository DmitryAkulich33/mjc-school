package com.epam.esm.dao.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.domain.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoleRepositoryTest {
    private static final Long ID_1 = 1L;
    private static final String ROLE_NAME = "ROLE_USER";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateRole() {
        Role role = new Role();
        role.setName(ROLE_NAME);
        Role actual = roleRepository.save(role);

        assert actual.getId().equals(ID_1);
        assert actual.getName().equals(role.getName());
    }

    @Test
    public void testFindByName() {
        Role expected = new Role();
        expected.setName(ROLE_NAME);
        entityManager.persist(expected);

        Role actual = roleRepository.findByName(ROLE_NAME).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByName_NotFound() {
        Optional<Role> expected = Optional.empty();

        Optional<Role> actual = roleRepository.findByName(ROLE_NAME);

        assertEquals(expected, actual);
    }
}
