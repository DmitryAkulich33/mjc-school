package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagDaoImplTest {
    private static final Long ID_1 = 1L;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_2 = 2;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;
    private static final Integer WRONG_OFFSET = -2;

    private Faker faker = new Faker();

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    @Autowired
    private TagDao tagDao;

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

        Tag actual = tagDao.getTagById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagById_NotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagDao.getTagById(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName() {
        Tag expected = entityManager.persist(tag1);

        Tag actual = tagDao.getTagByName(expected.getName()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName_NotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagDao.getTagByName(TAG_NAME_1);

        assertEquals(expected, actual);
    }


    @Test
    public void testGetTags() {
        Tag expected1 = entityManager.persist(tag1);
        Tag expected2 = entityManager.persist(tag2);
        List<Tag> expected = Arrays.asList(expected1, expected2);

        List<Tag> actual = tagDao.getTags();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_Pagination() {
        Tag expected1 = entityManager.persist(tag1);
        entityManager.persist(tag2);
        List<Tag> expected = singletonList(expected1);

        List<Tag> actual = tagDao.getTags(OFFSET_0, PAGE_SIZE_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_Pagination_WrongEnteredDataException() {
        entityManager.persist(tag1);
        entityManager.persist(tag2);
        assertThrows(WrongEnteredDataException.class, () -> {
            tagDao.getTags(OFFSET_2, PAGE_SIZE_2);
        });
    }

    @Test
    public void testGetTags_TagDaoException() {
        assertThrows(TagDaoException.class, () -> {
            tagDao.getTags(WRONG_OFFSET, PAGE_SIZE_2);
        });
    }

    @Test
    public void testDeleteTags() {
        entityManager.persist(tag1);

        tagDao.deleteTag(tag1.getId());

        TypedQuery<Tag> query = entityManager.getEntityManager().createQuery(
                "SELECT t FROM tag t WHERE t.id=?1 AND t.lock=0", Tag.class);
        Tag actual = query.setParameter(1, tag1.getId()).getResultStream().findAny().orElse(null);

        assertNull(actual);
    }

    @Test
    public void testCreateTag() {
        Tag actual = tagDao.createTag(tag3);

        assert actual.getId().equals(ID_1);
        assert actual.getName().equals(tag3.getName());
        assert actual.getLock().equals(LOCK);
    }

    @Test
    public void testCreateTag_TagDuplicateException() {
        entityManager.persist(tag1);
        assertThrows(TagDuplicateException.class, () -> {
            tagDao.createTag(tag3);
        });
    }

    @Test
    void testGetMostWidelyUsedTagsOfUsersWithHighestCostOfOrders() {
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        Tag tag3 = createTag();
        Tag tag4 = createTag();
        Certificate certificate1 = createCertificate(singletonList(tag1), 40.0);
        Certificate certificate2 = createCertificate(singletonList(tag2), 25.0);
        Certificate certificate3 = createCertificate(singletonList(tag3), 10.0);
        Certificate certificate4 = createCertificate(singletonList(tag4), 10.0);
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        createOrder(certificate1, user1);
        createOrder(certificate2, user1);
        createOrder(certificate3, user1);

        createOrder(certificate2, user2);

        createOrder(certificate1, user3);
        createOrder(certificate2, user3);
        createOrder(certificate4, user3);

        List<Tag> expected = Arrays.asList(tag1, tag2);

        List<Tag> actual = tagDao.getMostWidelyUsedTagsOfUsersWithHighestCostOfOrders().get();

        actual.sort(Comparator.comparing(Tag::getId));
        assertEquals(expected, actual);
    }

    private void createOrder(Certificate certificate, User user) {
        Order order = new Order();
        order.setTotal(Stream.of(certificate).mapToDouble(Certificate::getPrice).sum());
        order.setUser(user);
        order.setCertificates(Stream.of(certificate).collect(Collectors.toList()));
        entityManager.persist(order);
    }

    private User createUser() {
        User user = new User();
        user.setName(faker.name().firstName());
        user.setSurname(faker.name().lastName());
        entityManager.persist(user);
        return user;
    }

    private Certificate createCertificate(List<Tag> tags, Double price) {
        Certificate certificate = new Certificate();
        certificate.setName(faker.book().title());
        certificate.setDescription(faker.shakespeare().romeoAndJulietQuote());
        certificate.setPrice(price);
        certificate.setDuration(faker.random().nextInt(100));
        certificate.setTags(tags);
        entityManager.persist(certificate);
        return certificate;
    }

    private Tag createTag() {
        Tag tag = new Tag();
        tag.setName(faker.food().ingredient());
        entityManager.persist(tag);
        return tag;
    }
}