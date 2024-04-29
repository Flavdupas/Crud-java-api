package com.aori.mysql.controllers;

import com.aori.mysql.TestDataUtil;
import com.aori.mysql.domain.dto.AuthorDto;
import com.aori.mysql.domain.entities.AuthorEntity;
import com.aori.mysql.services.AuthorServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegration {

    private AuthorServices authorServices;
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Autowired
    public AuthorControllerIntegration(AuthorServices authorServices, MockMvc mockMvc) {
        this.authorServices = authorServices;
        this.mockMvc = mockMvc;
        this.mapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccefullyReturnsHttp201Created() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String authorJson = mapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.post("/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateAuthorSuccefullyReturnsSavedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String authorJson = mapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.post("/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()))
        ;
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorServices.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(author.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(author.getAge()));
    }

    @Test
    public void testThatAuthorReturnsAuthorWhenAuthorExist() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorServices.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/" + author.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExist() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorServices.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/" + author.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()));
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttp404WhenNoAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        String authorJson = mapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorServices.save(author);
        String authorJson = mapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorServices.save(author);

        AuthorEntity updatedAuthor = TestDataUtil.createTestAuthorTwo();
        updatedAuthor.setId(author.getId());

        String authorJson = mapper.writeValueAsString(updatedAuthor);

        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedAuthor.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(updatedAuthor.getAge()));
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200Ok() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorServices.save(author);

        savedAuthor.setName("Ariana Grande");

        String authorJson = mapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorServices.save(author);

        savedAuthor.setName("Ariana Grande");

        String authorJson = mapper.writeValueAsString(author);

        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()));
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorServices.save(author);

        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/" + author.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
