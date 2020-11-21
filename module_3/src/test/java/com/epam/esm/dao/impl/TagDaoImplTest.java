package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.WrongEnteredDataException;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagDaoImplTest {
    private static final Long ID_1 = 1L;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final String CERTIFICATE_DESCRIPTION_1 = "Certificate for one going to the shop";
    private static final String CERTIFICATE_DESCRIPTION_2 = "Food and drink without check limit at Viet Express";
    private static final Double CERTIFICATE_PRICE_1 = 50.0;
    private static final Double CERTIFICATE_PRICE_2 = 100.0;
    private static final Integer CERTIFICATE_DURATION_1 = 365;
    private static final Integer CERTIFICATE_DURATION_2 = 100;
    private static final String USER_NAME_1 = "Ivan";
    private static final String USER_NAME_2 = "Petr";
    private static final String USER_SURNAME_1 = "Ivanov";
    private static final String USER_SURNAME_2 = "Petrov";
    private static final String ORDER_DATE_1 = "2020-11-22T10:45:11";
    private static final String ORDER_DATE_2 = "2020-11-22T12:45:11";
    private static final Double ORDER_TOTAL_1 = 150.0;
    private static final Double ORDER_TOTAL_2 = 50.0;
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_2 = 2;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;
    private static final Integer WRONG_OFFSET = -2;

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    private Certificate certificate1;
    private Certificate certificate2;

    private User user1;
    private User user2;

    private Order order1;
    private Order order2;

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

        List<Tag> tags1 = Arrays.asList(tag1, tag2);
        List<Tag> tags2 = Collections.singletonList(tag1);

        certificate1 = new Certificate();
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription(CERTIFICATE_DESCRIPTION_1);
        certificate1.setPrice(CERTIFICATE_PRICE_1);
        certificate1.setDuration(CERTIFICATE_DURATION_1);
        certificate1.setTags(tags1);

        certificate2 = new Certificate();
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription(CERTIFICATE_DESCRIPTION_2);
        certificate2.setPrice(CERTIFICATE_PRICE_2);
        certificate2.setDuration(CERTIFICATE_DURATION_2);
        certificate2.setTags(tags2);

        List<Certificate> certificates1 = Arrays.asList(certificate1, certificate2);
        List<Certificate> certificates2 = Collections.singletonList(certificate1);

        user1 = new User();
        user1.setName(USER_NAME_1);
        user1.setSurname(USER_SURNAME_1);
        user1.setLock(LOCK);

        user2 = new User();
        user2.setName(USER_NAME_2);
        user2.setSurname(USER_SURNAME_2);
        user2.setLock(LOCK);

        order1 = new Order();
        order1.setPurchaseDate(LocalDateTime.parse(ORDER_DATE_1));
        order1.setTotal(ORDER_TOTAL_1);
        order1.setUser(user1);
        order1.setLock(LOCK);
        order1.setCertificates(certificates1);

        order2 = new Order();
        order2.setPurchaseDate(LocalDateTime.parse(ORDER_DATE_2));
        order2.setTotal(ORDER_TOTAL_2);
        order2.setUser(user2);
        order2.setLock(LOCK);
        order2.setCertificates(certificates2);
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
        Tag expected2 = entityManager.persist(tag2);
        List<Tag> expected = Collections.singletonList(expected1);

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
    public void getTheMostUsedTag() {
        entityManager.persist(tag1);
        entityManager.persist(tag2);
        entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(order1);
        entityManager.persist(order2);
        Tag expected = tag1;

        Tag actual = tagDao.getTheMostUsedTag();

        assertEquals(expected, actual);
    }
}