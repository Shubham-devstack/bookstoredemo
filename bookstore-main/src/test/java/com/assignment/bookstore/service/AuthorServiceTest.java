package com.assignment.bookstore.service;

import com.assignment.bookstore.entity.Author;
import com.assignment.bookstore.model.AuthorDTO;
import com.assignment.bookstore.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorRepository = Mockito.mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void testCreateAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("John Doe");
        authorDTO.setBirthday(LocalDate.of(1990, 1, 1));

        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setName("John Doe");
        savedAuthor.setBirthday(LocalDate.of(1990, 1, 1));

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        Author result = authorService.createAuthor(authorDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthday());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testFindByAuthorName() {
        String searchName = "John";
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        when(authorRepository.findByNameIgnoreCaseContaining(searchName))
                .thenReturn(List.of(author));

        List<Author> result = authorService.findByAuthorName(searchName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(authorRepository, times(1)).findByNameIgnoreCaseContaining(searchName);
    }

    @Test
    void testFindAuthorById() {
        long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("John Doe");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        Author result = authorService.findAuthorById(authorId);

        assertNotNull(result);
        assertEquals(authorId, result.getId());
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testFindAuthorByIdNotFound() {
        long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Author result = authorService.findAuthorById(authorId);

        assertNull(result);
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testCreateAuthors() {
        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.setName("John Doe");
        authorDTO1.setBirthday(LocalDate.of(1990, 1, 1));

        AuthorDTO authorDTO2 = new AuthorDTO();
        authorDTO2.setName("Jane Smith");
        authorDTO2.setBirthday(LocalDate.of(1985, 5, 20));

        Author savedAuthor1 = new Author();
        savedAuthor1.setId(1L);
        savedAuthor1.setName("John Doe");
        savedAuthor1.setBirthday(LocalDate.of(1990, 1, 1));

        Author savedAuthor2 = new Author();
        savedAuthor2.setId(2L);
        savedAuthor2.setName("Jane Smith");
        savedAuthor2.setBirthday(LocalDate.of(1985, 5, 20));

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor1, savedAuthor2);

        List<Author> result = authorService.createAuthors(Arrays.asList(authorDTO1, authorDTO2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(authorRepository, times(2)).save(any(Author.class));
    }

    @Test
    void testFindAll() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("John Doe");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Jane Smith");

        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        List<Author> result = authorService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(authorRepository, times(1)).findAll();
    }
}
