package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagDao {
    Tag createTag(Tag tag);

    void createTagCertificate(Long idTag, Long idCertificate);

    void deleteTag(Long idTag);

    Tag getTagById(Long idTag);

    Tag getTagByName(String nameTag);

    List<Tag> getTags();

    List<Tag> getCertificateTags(Long idCertificate);
}
