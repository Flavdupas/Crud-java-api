package com.aori.mysql.repositories;

import com.aori.mysql.TestDataUtil;
import com.aori.mysql.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTest {
    private AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTest(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        Optional<AuthorEntity> results = underTest.findById(authorEntity.getId());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatMulitpleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthorTwo();
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthorThree();
        //Insert authorEntity in the database
        underTest.save(authorEntity1);
        underTest.save(authorEntity2);
        underTest.save(authorEntity3);

        Iterable<AuthorEntity> results = underTest.findAll();
        assertThat(results).isNotEmpty();
        assertThat(results)
                .hasSize(3)
                .containsExactly(authorEntity1, authorEntity2, authorEntity3);
    }

    @Test
    public void testThatAuthorCanBeUpdatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        authorEntity.setName("New AuthorEntity");
        underTest.save(authorEntity);

        Optional<AuthorEntity> results = underTest.findById(authorEntity.getId());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatAuthorCanDeleteAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        underTest.deleteById(authorEntity.getId());
        Optional<AuthorEntity> results = underTest.findById(authorEntity.getId());
        assertThat(results).isEmpty();
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthorTwo();
        underTest.save(authorEntity2);
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthorThree();
        underTest.save(authorEntity3);

        Iterable<AuthorEntity> results = underTest.ageLessThan(70);
        assertThat(results).containsExactly(authorEntity2, authorEntity3);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthorTwo();
        underTest.save(authorEntity2);
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthorThree();
        underTest.save(authorEntity3);
        Iterable<AuthorEntity> results = underTest.findAuthorsWithAgeGreaterThan(70);
        assertThat(results).containsExactly(authorEntity);
    }
}
