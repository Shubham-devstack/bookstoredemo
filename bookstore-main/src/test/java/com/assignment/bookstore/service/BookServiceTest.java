package com.assignment.bookstore.service;

import com.assignment.bookstore.entity.Author;
import com.assignment.bookstore.entity.Book;
import com.assignment.bookstore.exception.EntityExistsException;
import com.assignment.bookstore.exception.BookNotFoundException;
import com.assignment.bookstore.model.AuthorDTO;
import com.assignment.bookstore.model.BookDTO;
import com.assignment.bookstore.model.BookUpdateDTO;
import com.assignment.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    private BookDTO bookDTO;
    private Book book;
    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        authorDTO = new AuthorDTO();
        authorDTO.setName("Test Author");

        bookDTO = new BookDTO();
        bookDTO.setIsbn("1234567890");
        bookDTO.setTitle("Test Book");
        bookDTO.setPrice(29.99);
        bookDTO.setYear(2025);
        bookDTO.setAuthors(Collections.singletonList(authorDTO));

        author = new Author();
        author.setName("Test Author");
        author.setId(1L);

        book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book.setPrice(29.99);
        book.setAuthors(Set.of(author));
    }

    @Test
    void addBook_Success() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());
        when(authorService.createAuthors(any())).thenReturn(Collections.singletonList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(bookDTO);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo(bookDTO.getIsbn());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBook_DuplicateIsbn_ThrowsException() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));

        assertThrows(EntityExistsException.class, () -> bookService.addBook(bookDTO));
    }

    @Test
    void updateBook_Success() {
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setPrice(39.99);
        updateDTO.setAuthors(Collections.singletonList(authorDTO));

        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));
        when(authorService.createAuthors(any())).thenReturn(Collections.singletonList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.updateBook("1234567890", updateDTO);

        assertThat(result).isNotNull();
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void findBookByIsbn_Success() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));

        Book result = bookService.findBookByIsbn("1234567890");

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void findBookByIsbn_NotFound_ThrowsException() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookByIsbn("1234567890"));
    }

    @Test
    void findBooks_Success() {
        when(bookRepository.findByTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(anyString(), anyString()))
            .thenReturn(Collections.singletonList(book));

        List<Book> results = bookService.findBooks("Test", "Author");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Test Book");
    }

    @Test
    void findAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book, book));

        List<Book> results = bookService.findAllBooks();

        assertThat(results).hasSize(2);
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(anyString());

        bookService.deleteBook("1234567890");

        verify(bookRepository).deleteById("1234567890");
    }

    @Test
    void deleteBook_NotFound_ThrowsException() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook("1234567890"));
    }
}
