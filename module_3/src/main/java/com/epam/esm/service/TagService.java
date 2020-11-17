package com.epam.esm.service;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);

    void deleteTag(Long idTag);

    Tag getTagById(Long idTag);

    List<Tag> getTags(Integer pageNumber, Integer pageSize);

    List<Tag> updateTags(List<Tag> tags);
}
