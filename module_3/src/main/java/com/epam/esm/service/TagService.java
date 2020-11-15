package com.epam.esm.service;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);

    void deleteTag(Long idTag);

    Tag getTagById(Long idTag);

    List<Tag> getTags();

    List<Tag> updateTags(List<Tag> tags, Long idCertificate);

    boolean isNewTag(List<Tag> tags, String nameTag);
}
