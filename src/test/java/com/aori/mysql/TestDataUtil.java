package com.aori.mysql;

import com.aori.mysql.domain.dto.AuthorDto;
import com.aori.mysql.domain.dto.BookDto;
import com.aori.mysql.domain.entities.AuthorEntity;
import com.aori.mysql.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil() {}


    public static AuthorEntity createTestAuthor() {
        return AuthorEntity.builder().name("Abigail Rose").age(80).build();
    }

    public static AuthorEntity createTestAuthorTwo() {
        return AuthorEntity.builder().name("Amélie Noton").age(53).build();
    }

    public static AuthorEntity createTestAuthorThree() {
        return AuthorEntity.builder().name("Haruki Murakami").age(62).build();
    }

    public static BookEntity createTestBook(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("The shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }
    public static BookDto createTestBookDto(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-0")
                .title("The shadow in the Attic")
                .author(authorDto)
                .build();
    }

    public static BookEntity createTestBookTwo(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("979-1-2345-6789-1")
                .title("Barbe bleue")
                .authorEntity(authorEntity)
                .build();
    }
    public static BookDto createTestBookDtoTwo(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("979-1-2345-6789-1")
                .title("Barbe bleue")
                .author(authorDto)
                .build();
    }

    public static BookEntity createTestBookThree(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("980-1-2345-6789-2")
                .title("Norwegian Wood")
                .authorEntity(authorEntity)
                .build();
    }
    public static BookDto createTestBookDtoThree(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("980-1-2345-6789-2")
                .title("Norwegian Wood")
                .author(authorDto)
                .build();
    }
}
