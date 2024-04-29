package com.aori.mysql.services;

import com.aori.mysql.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorServices {
    AuthorEntity save(AuthorEntity authorEntity) ;

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> find(Long id);

    boolean isExists(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void delete(Long id);
}
