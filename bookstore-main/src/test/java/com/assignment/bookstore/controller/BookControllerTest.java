package com.assignment.bookstore.controller;

import com.assignment.bookstore.entity.Book;
import com.assignment.bookstore.model.AuthorDTO;
import com.assignment.bookstore.model.BookDTO;
import com.assignment.bookstore.model.BookUpdateDTO;
import com.assignment.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;
    private BookDTO testBookDTO;
    private BookUpdateDTO testBookUpdateDTO;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setIsbn("123-456-789");
        testBook.setTitle("Test Book");
        testBook.setYear(2023);
        testBook.setPrice(10D);
        testBook.setGenre("Fiction");

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Test Author");
        authorDTO.setBirthday(LocalDate.of(1990, 1, 1));

        testBookDTO = new BookDTO();
        testBookDTO.setIsbn("123-456-789");
        testBookDTO.setTitle("Test Book");
        testBookDTO.setYear(2022);
        testBookDTO.setPrice(1000D);
        testBookDTO.setGenre("Non-Fiction");
        testBookDTO.setAuthors(Collections.singletonList(authorDTO));

        testBookUpdateDTO = new BookUpdateDTO();
        testBookUpdateDTO.setYear(2025);
        testBookUpdateDTO.setPrice(100);
        testBookUpdateDTO.setGenre("Drama");
        testBookUpdateDTO.setTitle("Updated Test Book");
    }

    @Test
    @WithMockUser
    void createBookCatalogEntry_Success() throws Exception {
        when(bookService.addBook(any(BookDTO.class))).thenReturn(testBook);

        mockMvc.perform(post("/api/v1/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(testBook.getIsbn()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createBookCatalogEntry_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getBookCatalog_Success() throws Exception {
        when(bookService.findAllBooks()).thenReturn(Arrays.asList(testBook));

        mockMvc.perform(get("/api/v1/books/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value(testBook.getIsbn()));
    }

    @Test
    @WithMockUser
    void getBookDetails_Success() throws Exception {
        when(bookService.findBookByIsbn(testBook.getIsbn())).thenReturn(testBook);

        mockMvc.perform(get("/api/v1/books/{isbn}", testBook.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(testBook.getIsbn()));
    }

    @Test
    @WithMockUser
    void searchBookCatalog_ByTitle() throws Exception {
        when(bookService.findBooks("Test Book", null))
                .thenReturn(Collections.singletonList(testBook));

        mockMvc.perform(get("/api/v1/books/search")
                        .param("title", "Test Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value(testBook.getIsbn()));
    }

    @Test
    @WithMockUser
    void updateBookCatalogEntry_Success() throws Exception {
        when(bookService.updateBook(eq(testBook.getIsbn()), any(BookUpdateDTO.class)))
                .thenReturn(testBook);

        mockMvc.perform(put("/api/v1/books/{isbn}", testBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookUpdateDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(testBook.getIsbn()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeBookFromCatalog_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/books/{isbn}", testBook.getIsbn())
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(testBook.getIsbn());
    }

    @Test
    @WithMockUser
    void searchBookCatalog_NoParams_ReturnsEmptyList() throws Exception {
        when(bookService.findBooks(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/books/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
