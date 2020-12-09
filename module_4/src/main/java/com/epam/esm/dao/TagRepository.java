package com.epam.esm.dao;

import com.epam.esm.domain.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends ParentEntityRepository<Tag> {
    Optional<Tag> findByName(String name);
}
