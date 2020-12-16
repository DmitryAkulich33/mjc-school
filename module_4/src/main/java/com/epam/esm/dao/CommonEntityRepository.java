package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonEntityRepository<T> extends CrudRepository<T, Long> {

    @Query("select t from #{#entityName} t where t.deleted = ?1")
    List<T> getEntities(Boolean deleted, Pageable pageable);

    @Query("select t from #{#entityName} t where t.deleted = ?1")
    List<T> getEntities(Boolean deleted);

    @Query("select t from #{#entityName} t where t.deleted = ?1 AND t.id = ?2")
    Optional<T> getEntityById(Boolean deleted, Long id);

    @Modifying
    @Query("update #{#entityName} t set t.deleted = ?1 where t.id = ?2")
    void deleteEntity(Boolean deleted, Long id);
}
