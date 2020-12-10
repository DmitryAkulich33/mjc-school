package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
//    private static final Long TAG_ID = 1L;
//    private static final Integer PAGE_NUMBER = 1;
//    private static final Integer PAGE_SIZE = 10;
//    private static final Integer OFFSET = 0;
//
//    @Mock
//    private TagDao mockTagDao;
//    @Mock
//    private UserDao mockUserDao;
//
//    @InjectMocks
//    private TagServiceImpl tagService;
//
//    @Test
//    public void testGetTagById() {
//        Tag expected = new Tag();
//        expected.setId(TAG_ID);
//
//        when(mockTagDao.getTagById(TAG_ID)).thenReturn(Optional.of(expected));
//
//        Tag actual = tagService.getTagById(TAG_ID);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetTagById_TagNotFoundException() {
//        when(mockTagDao.getTagById(TAG_ID)).thenReturn(Optional.empty());
//
//        assertThrows(TagNotFoundException.class, () -> {
//            tagService.getTagById(TAG_ID);
//        });
//    }
//
//    @Test
//    public void testGetTagById_TagDaoException() {
//        when(mockTagDao.getTagById(TAG_ID)).thenThrow(new TagDaoException());
//
//        assertThrows(TagDaoException.class, () -> {
//            tagService.getTagById(TAG_ID);
//        });
//    }
//
//    @Test
//    public void testCreateTag() {
//        Tag expected = new Tag();
//
//        when(mockTagDao.createTag(expected)).thenReturn(expected);
//
//        Tag actual = tagService.createTag(expected);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testCreateTag_TagDaoException() {
//        Tag expected = new Tag();
//        when(mockTagDao.createTag(expected)).thenThrow(new TagDaoException());
//
//        assertThrows(TagDaoException.class, () -> {
//            tagService.createTag(expected);
//        });
//    }
//
//    @Test
//    public void testCreateTag_TagDuplicateException() {
//        Tag expected = new Tag();
//        when(mockTagDao.createTag(expected)).thenThrow(new TagDuplicateException());
//
//        assertThrows(TagDuplicateException.class, () -> {
//            tagService.createTag(expected);
//        });
//    }
//
//    @Test
//    public void testGetTags_WithoutPagination() {
//        List<Tag> expected = new ArrayList<>();
//
//        when(mockTagDao.getTags()).thenReturn(expected);
//
//        List<Tag> actual = tagService.getTags(null, null);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetTags_WithoutPagination_TagDaoException() {
//        when(mockTagDao.getTags()).thenThrow(new TagDaoException());
//
//        assertThrows(TagDaoException.class, () -> {
//            tagService.getTags(null, null);
//        });
//    }
//
//    @Test
//    public void testGetTags_WithPagination() {
//        List<Tag> expected = new ArrayList<>();
//
//        when(mockTagDao.getTags(OFFSET, PAGE_SIZE)).thenReturn(expected);
//
//        List<Tag> actual = tagService.getTags(PAGE_NUMBER, PAGE_SIZE);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetTags_WithPagination_TagDaoException() {
//        when(mockTagDao.getTags(OFFSET, PAGE_SIZE)).thenThrow(new TagDaoException());
//
//        assertThrows(TagDaoException.class, () -> {
//            tagService.getTags(PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetTags_WithPagination_WrongEnteredDataException() {
//        when(mockTagDao.getTags(OFFSET, PAGE_SIZE)).thenThrow(new WrongEnteredDataException());
//
//        assertThrows(WrongEnteredDataException.class, () -> {
//            tagService.getTags(PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetTags_WithNullPageNumber_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            tagService.getTags(null, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetTags_WithNullPageSize_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            tagService.getTags(PAGE_NUMBER, null);
//        });
//    }
//
//    @Test
//    public void testUpdateTags() {
//        List<Tag> updateTags = new ArrayList<>();
//
//        List<Tag> expected = new ArrayList<>();
//
//        List<Tag> actual = tagService.updateTags(updateTags);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testDeleteTag() {
//        Tag expected = new Tag();
//        expected.setId(TAG_ID);
//
//        doReturn(Optional.of(expected)).when(mockTagDao).getTagById(TAG_ID);
//        tagService.deleteTag(TAG_ID);
//
//        verify(mockTagDao).getTagById(TAG_ID);
//        verify(mockTagDao).deleteTag(TAG_ID);
//    }
//
//    @Test
//    public void testDeleteTag_TagNotFoundException() {
//        when(mockTagDao.getTagById(TAG_ID)).thenReturn(Optional.empty());
//
//        assertThrows(TagNotFoundException.class, () -> {
//            tagService.deleteTag(TAG_ID);
//        });
//    }
//
//    @Test
//    public void testDeleteTag_TagDaoException() {
//        when(mockTagDao.getTagById(TAG_ID)).thenThrow(new TagDaoException());
//
//        assertThrows(TagDaoException.class, () -> {
//            tagService.deleteTag(TAG_ID);
//        });
//    }
//
//    @Test
//    public void testGetTheMostUsedTag_UserDaoException() {
//        when(mockUserDao.getUserWithTheLargeSumOrders()).thenThrow(new UserDaoException());
//
//        assertThrows(UserDaoException.class, () -> {
//            tagService.getTheMostUsedTag();
//        });
//    }
}