package com.epam.esm.service.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final Long TAG_ID = 1L;
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Long COUNT = 5L;

    @Mock
    private TagRepository mockTagRepository;
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    public void testGetTagById() {
        Tag expected = new Tag();
        expected.setId(TAG_ID);

        when(mockTagRepository.getEntityById(false, TAG_ID)).thenReturn(Optional.of(expected));

        Tag actual = tagService.getTagById(TAG_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagById_TagNotFoundException() {
        when(mockTagRepository.getEntityById(false, TAG_ID)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> {
            tagService.getTagById(TAG_ID);
        });
    }

    @Test
    public void testCreateTag() {
        Tag expected = new Tag();

        when(mockTagRepository.save(expected)).thenReturn(expected);

        Tag actual = tagService.createTag(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateTag_TagDuplicateException() {
        Tag expected = new Tag();
        when(mockTagRepository.save(expected)).thenThrow(new TagDuplicateException());

        assertThrows(TagDuplicateException.class, () -> {
            tagService.createTag(expected);
        });
    }

    @Test
    public void testGetTags_WithoutPagination() {
        List<Tag> expected = new ArrayList<>();

        when(mockTagRepository.getEntities(false)).thenReturn(expected);

        List<Tag> actual = tagService.getTags(null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_WithPagination() {
        List<Tag> expected = new ArrayList<>();

        when(mockTagRepository.getEntities(false, PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(expected);
        when(mockTagRepository.count()).thenReturn(COUNT);

        List<Tag> actual = tagService.getTags(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags_WithNullPageNumber_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            tagService.getTags(null, PAGE_SIZE);
        });
    }

    @Test
    public void testGetTags_WithNullPageSize_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            tagService.getTags(PAGE_NUMBER, null);
        });
    }

    @Test
    public void testUpdateTags() {
        List<Tag> updateTags = new ArrayList<>();

        List<Tag> expected = new ArrayList<>();

        List<Tag> actual = tagService.updateTags(updateTags);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTag() {
        Tag expected = new Tag();
        expected.setId(TAG_ID);

        doReturn(Optional.of(expected)).when(mockTagRepository).getEntityById(false, TAG_ID);
        tagService.deleteTag(TAG_ID);

        verify(mockTagRepository).getEntityById(false, TAG_ID);
        verify(mockTagRepository).deleteEntity(true, TAG_ID);
    }

    @Test
    public void testDeleteTag_TagNotFoundException() {
        when(mockTagRepository.getEntityById(false, TAG_ID)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> {
            tagService.deleteTag(TAG_ID);
        });
    }
}