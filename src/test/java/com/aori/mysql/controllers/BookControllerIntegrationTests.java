package com.aori.mysql.controllers;

import com.aori.mysql.TestDataUtil;
import com.aori.mysql.domain.dto.BookDto;
import com.aori.mysql.domain.entities.BookEntity;
import com.aori.mysql.services.BookService;
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
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnHttpStatus201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(createBookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testThatCreateBookReturnCreatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON).content(createBookJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle()));

    }

    @Test
    public void testThatListBooksReturnHttpStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListBookReturnsBook() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(),book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(book.getTitle()));
    }

    @Test
    public void testThatGetBookReturnHttpStatus200() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(),book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetBookReturnBookWhenBookExists() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(),book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatUpdateBookReturnHttpStatus20OOk() throws Exception {
        BookEntity book = TestDataUtil.createTestBookTwo(null);
        BookEntity savedBook = bookService.createUpdateBook(book.getIsbn(),book);
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        bookDto.setIsbn(savedBook.getIsbn());
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(createBookJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testThatPartialUpdateBookReturnHttpStatus20OOk() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        BookEntity savedBook = bookService.createUpdateBook(book.getIsbn(),book);
        BookDto bookDto = TestDataUtil.createTestBookDtoTwo(null);
        bookDto.setTitle(savedBook.getTitle());
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testThatPartialUpdateBookReturnUpdatedBook() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        BookEntity savedBook = bookService.createUpdateBook(book.getIsbn(),book);
        BookDto bookDto = TestDataUtil.createTestBookDtoTwo(null);
        bookDto.setTitle(savedBook.getTitle());
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(savedBook.getTitle()));
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(),book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
