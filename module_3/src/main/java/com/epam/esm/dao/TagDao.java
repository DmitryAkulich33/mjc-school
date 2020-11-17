package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    Tag createTag(Tag tag);

    void deleteTag(Long idTag);

    Optional<Tag> getTagById(Long idTag);

    Optional<Tag> getTagByName(String name);

    List<Tag> getTags(Integer offset, Integer pageSize);
}
