package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.TagValidatorException;
import com.epam.esm.util.TagValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final Long TAG_ID = 1L;

    @Mock
    private List<Tag> mockTags;
    @Mock
    private TagDao mockTagDao;
    @Mock
    private TagValidator mockTagValidator;

    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @Test
    public void testDeleteTagById() {
        tagServiceImpl.deleteTag(TAG_ID);

        verify(mockTagValidator).validateTagId(TAG_ID);
        verify(mockTagDao).getTagById(TAG_ID);
        verify(mockTagDao).deleteTag(TAG_ID);
    }

    @Test
    public void testDeleteTagById_TagNotFoundException() {
        doThrow(new TagNotFoundException()).when(mockTagDao).deleteTag(TAG_ID);

        assertThrows(TagNotFoundException.class, () -> {
            tagServiceImpl.deleteTag(TAG_ID);
        });
    }

    @Test
    public void testDeleteTagById_TagDaoException() {
        doThrow(new TagDaoException()).when(mockTagDao).deleteTag(TAG_ID);

        assertThrows(TagDaoException.class, () -> {
            tagServiceImpl.deleteTag(TAG_ID);
        });
    }

    @Test
    public void testDeleteTagById_TagValidatorException() {
        doThrow(new TagValidatorException()).when(mockTagDao).deleteTag(TAG_ID);

        assertThrows(TagValidatorException.class, () -> {
            tagServiceImpl.deleteTag(TAG_ID);
        });
    }

    @Test
    public void testGetTagById() {
        Tag expected = mock(Tag.class);

        when(mockTagDao.getTagById(TAG_ID)).thenReturn(expected);

        Tag actual = tagServiceImpl.getTagById(TAG_ID);

        assertEquals(expected, actual);
        verify(mockTagValidator).validateTagId(TAG_ID);
    }

    @Test
    public void testGetTagById_TagNotFoundException() {
        when(mockTagDao.getTagById(TAG_ID)).thenThrow(new TagNotFoundException());

        assertThrows(TagNotFoundException.class, () -> {
            tagServiceImpl.getTagById(TAG_ID);
        });
    }

    @Test
    public void testGetTagById_TagValidatorException() {
        when(mockTagDao.getTagById(TAG_ID)).thenThrow(new TagValidatorException());

        assertThrows(TagValidatorException.class, () -> {
            tagServiceImpl.getTagById(TAG_ID);
        });
    }

    @Test
    public void testGetAllTags() {
        when(mockTagDao.getTags()).thenReturn(mockTags);

        List<Tag> actual = tagServiceImpl.getTags();

        assertEquals(mockTags, actual);
    }

    @Test
    public void testCreateTag() {
        Tag expected = mock(Tag.class);

        when(mockTagDao.createTag(expected)).thenReturn(expected);

        Tag actual = tagServiceImpl.createTag(expected);

        assertEquals(expected, actual);
        verify(mockTagValidator).validateTagName(expected.getName());
        verify(mockTagDao).createTag(expected);
    }

    @Test
    public void testCreateTag_TagValidatorException() {
        Tag mockTag = mock(Tag.class);

        when(mockTagDao.createTag(mockTag)).thenThrow(new TagValidatorException());

        assertThrows(TagValidatorException.class, () -> {
            tagServiceImpl.createTag(mockTag);
        });
    }
}