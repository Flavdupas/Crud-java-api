package com.aori.mysql.controllers;

import com.aori.mysql.domain.dto.AuthorDto;
import com.aori.mysql.domain.entities.AuthorEntity;
import com.aori.mysql.mappers.Mapper;
import com.aori.mysql.services.AuthorServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorServices authorServices;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorServices authorServices, Mapper<AuthorEntity, AuthorDto> mapper) {
        this.authorServices = authorServices;
        this.authorMapper = mapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorServices.save(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED) ;
    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
       List<AuthorEntity> authors = authorServices.findAll();
       return authors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
    }
    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id) throws Exception {
        Optional<AuthorEntity> author = authorServices.find(id);
        return author.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto author) {
        if(!authorServices.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        author.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorServices.save(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto author) {
        if(!authorServices.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity updatedAuthor = authorServices.partialUpdate(id,authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(updatedAuthor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable("id") Long id) {
        authorServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
