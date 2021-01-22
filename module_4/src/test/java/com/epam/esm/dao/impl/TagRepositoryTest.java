package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.domain.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagRepositoryTest {
    private static final Long ID_1 = 1L;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_NUMBER_1 = 0;

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        tag3 = new Tag();
        tag3.setName(TAG_NAME_1);
    }

    @Test
    public void testGetTagById() {
        Tag expected = entityManager.persist(tag1);

        Tag actual = tagRepository.getEntityById(false, expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagById_NotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagRepository.getEntityById(false, ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName() {
        Tag expected = entityManager.persist(tag1);

        Tag actual = tagRepository.findByName(expected.getName()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName_NotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagRepository.findByName(TAG_NAME_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags() {
        Tag expected1 = entityManager.persist(tag1);
        Tag expected2 = entityManager.persist(tag2);
        List<Tag> expected = Arrays.asList(expected1, expected2);

        List<Tag> actual = tagRepository.getEntities(false);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_Pagination() {
        Tag expected1 = entityManager.persist(tag1);
        entityManager.persist(tag2);
        List<Tag> expected = Collections.singletonList(expected1);

        List<Tag> actual = tagRepository.getEntities(false, PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_1));

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTags() {
        entityManager.persist(tag1);

        tagRepository.deleteEntity(true, tag1.getId());

        TypedQuery<Tag> query = entityManager.getEntityManager().createQuery(
                "SELECT t FROM tag t WHERE t.id = ?1 AND t.deleted = false ", Tag.class);
        Tag actual = query.setParameter(1, tag1.getId()).getResultStream().findAny().orElse(null);

        assertNull(actual);
    }

    @Test
    public void testCreateTag() {
        Tag actual = tagRepository.save(tag3);

        assert actual.getId().equals(ID_1);
        assert actual.getName().equals(tag3.getName());
        assert actual.getDeleted().equals(false);
    }
}