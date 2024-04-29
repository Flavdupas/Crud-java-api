package com.aori.mysql.repositories;

import com.aori.mysql.TestDataUtil;
import com.aori.mysql.domain.entities.AuthorEntity;
import com.aori.mysql.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTest {

    private AuthorRepository authorRepository;
    private BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTest(BookRepository underTest, AuthorRepository author) {
        this.underTest = underTest;
        this.authorRepository = author;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        authorRepository.save(authorEntity);
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> results = underTest.findById(bookEntity.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthorTwo();
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthorThree();

        //create authorEntity
        authorRepository.save(authorEntity);
        authorRepository.save(authorEntity2);
        authorRepository.save(authorEntity3);

        //Create instance of book
        BookEntity bookEntity1 = TestDataUtil.createTestBook(authorEntity);
        BookEntity bookEntity2 = TestDataUtil.createTestBookTwo(authorEntity2);
        BookEntity bookEntity3 = TestDataUtil.createTestBookThree(authorEntity3);

        //create book
        underTest.save(bookEntity1);
        underTest.save(bookEntity2);
        underTest.save(bookEntity3);

        Iterable<BookEntity> results = underTest.findAll();
        assertThat(results).isNotEmpty();
        assertThat(results)
                .hasSize(3)
                .containsExactly(bookEntity1, bookEntity2, bookEntity3);
    }

    @Test
    public void testThatBookCanBeUpdatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorTwo();
        authorRepository.save(authorEntity);
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        underTest.save(bookEntity);

        Optional<BookEntity> results = underTest.findById(bookEntity.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeletedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        authorRepository.save(authorEntity);

        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        underTest.save(bookEntity);

        underTest.deleteById(bookEntity.getIsbn());
        Optional<BookEntity> results = underTest.findById(bookEntity.getIsbn());
        assertThat(results).isEmpty();
    }
}
