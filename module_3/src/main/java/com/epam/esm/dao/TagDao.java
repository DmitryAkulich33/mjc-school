package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    Tag createTag(Tag tag);

    void deleteTag(Long idTag);

    Tag getTagById(Long idTag);

    Tag getTagByName(String nameTag);

    List<Tag> getTags(Integer offset, Integer pageSize);

    List<Tag> getTags();
}
