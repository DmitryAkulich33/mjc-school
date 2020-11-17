package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
class TagDaoImplTest {
    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long TAG_ID_TO_CREATE = 3L;
    private static final Long WRONG_ID = 10L;
    private static final String WRONG_NAME = "super";
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String TAG_NAME_TO_CREATE = "spa";
    private static final Integer TAG_LOCK = 0;

    private Tag tag1;
    private Tag tag2;
    private Tag tagFromQuery;
    private Tag tagFromDb;


    @Autowired
    private TagDao tagDao;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag();
        tag1.setId(CORRECT_ID_1);
        tag1.setName(TAG_NAME_1);
        tag1.setLock(TAG_LOCK);

        tag2 = new Tag();
        tag2.setId(CORRECT_ID_2);
        tag2.setName(TAG_NAME_2);
        tag2.setLock(TAG_LOCK);

        tagFromQuery = new Tag();
        tagFromQuery.setName(TAG_NAME_TO_CREATE);

        tagFromDb = new Tag();
        tagFromDb.setId(TAG_ID_TO_CREATE);
        tagFromDb.setName(TAG_NAME_TO_CREATE);
        tagFromDb.setLock(TAG_LOCK);
    }

//    @Test
//    public void testGetTagById() {
//        Tag expected = tag1;
//
//        Tag actual = tagDao.getTagById(CORRECT_ID_1);
//
//        Assertions.assertEquals(expected, actual);
//    }

//    @Test
//    public void testGetTagById_TagNotFoundException() {
//        assertThrows(TagNotFoundException.class, () -> {
//            tagDao.getTagById(WRONG_ID);
//        });
//    }
//
//    @Test
//    public void testCreateTag() {
//        Tag tagForCreate = new Tag();
//        tagForCreate.setName("new");
//
//        Tag actual = tagDao.createTag(tagForCreate);
//
//        Tag expected = new Tag();
//        expected.setId(3L);
//        expected.setName("new");
//        expected.setLock(0);
//
//        Assertions.assertEquals(expected, actual);
//    }

}