package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagDaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
@Transactional
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
class TagDaoImplTest {
    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long TAG_ID_TO_CREATE = 3L;
    private static final Long WRONG_ID = 100L;
    private static final String WRONG_NAME = "super";
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String TAG_NAME_TO_CREATE = "spa";
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET = 0;

    private Tag tag1;
    private Tag tag2;
    private Tag correctTagFromQuery;
    private Tag wrongTagFromQuery;
    private Tag tagFromDb;

    @Autowired
    private TagDao tagDao;

    @BeforeEach
    void setUp() {
        tag1 = new Tag();
        tag1.setId(CORRECT_ID_1);
        tag1.setName(TAG_NAME_1);
        tag1.setLock(LOCK);

        tag2 = new Tag();
        tag2.setId(CORRECT_ID_2);
        tag2.setName(TAG_NAME_2);
        tag2.setLock(LOCK);

        correctTagFromQuery = new Tag();
        correctTagFromQuery.setName(TAG_NAME_TO_CREATE);

        wrongTagFromQuery = new Tag();
        wrongTagFromQuery.setName(TAG_NAME_2);

        tagFromDb = new Tag();
        tagFromDb.setId(TAG_ID_TO_CREATE);
        tagFromDb.setName(TAG_NAME_TO_CREATE);
        tagFromDb.setLock(LOCK);
    }

    @Test
    public void testGetTagById() {
        Optional<Tag> expected = Optional.ofNullable(tag1);

        Optional<Tag> actual = tagDao.getTagById(CORRECT_ID_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTagById_WrongResult() {
        Optional<Tag> expected = Optional.ofNullable(tag2);

        Optional<Tag> actual = tagDao.getTagById(CORRECT_ID_1);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetTagById_NotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagDao.getTagById(WRONG_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName() {
        Optional<Tag> expected = Optional.ofNullable(tag1);

        Optional<Tag> actual = tagDao.getTagByName(TAG_NAME_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName_WrongResult() {
        Optional<Tag> expected = Optional.ofNullable(tag2);

        Optional<Tag> actual = tagDao.getTagByName(TAG_NAME_1);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetTagByName_NotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagDao.getTagByName(WRONG_NAME);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTags() {
        List<Tag> expected = new ArrayList<>(Arrays.asList(tag1, tag2));

        List<Tag> actual = tagDao.getTags(OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_Pagination() {
        List<Tag> expected = new ArrayList<>(Arrays.asList(tag1));

        List<Tag> actual = tagDao.getTags(OFFSET, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_WrongResult() {
        List<Tag> expected = new ArrayList<>(Arrays.asList(tag1, tag2));

        List<Tag> actual = tagDao.getTags(OFFSET, PAGE_SIZE_1);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testDeleteTags() {
        List<Tag> expected = new ArrayList<>(Collections.singletonList(tag1));

        tagDao.deleteTag(CORRECT_ID_2);

        List<Tag> actual = tagDao.getTags(OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTags_WrongResult() {
        List<Tag> expected = new ArrayList<>(Collections.singletonList(tag1));

        tagDao.deleteTag(CORRECT_ID_1);

        List<Tag> actual = tagDao.getTags(OFFSET, PAGE_SIZE_10);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testCreateTag() {
        Tag actual = tagDao.createTag(correctTagFromQuery);

        Assertions.assertEquals(tagFromDb, actual);
    }

    @Test
    public void testCreateTag_WrongResult() {
        Tag actual = tagDao.createTag(correctTagFromQuery);

        Assertions.assertNotEquals(tag1, actual);
    }

    @Test
    public void testCreateTag_TagDaoException() {
        assertThrows(TagDaoException.class, () -> {
            tagDao.createTag(wrongTagFromQuery);
        });
    }
}