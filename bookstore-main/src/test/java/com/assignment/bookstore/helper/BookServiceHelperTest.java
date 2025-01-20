package com.assignment.bookstore.helper;

import com.assignment.bookstore.entity.Author;
import com.assignment.bookstore.entity.Book;
import com.assignment.bookstore.exception.InvalidInputException;
import com.assignment.bookstore.model.BookDTO;
import com.assignment.bookstore.model.BookUpdateDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceHelperTest {

    @Test
    void testValidateRequestDTO_ValidInput() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("123-4567890123");
        bookDTO.setYear(2023);

        assertDoesNotThrow(() -> BookServiceHelper.validateRequestDTO(bookDTO));
    }

    @Test
    void testValidateRequestDTO_InvalidISBN() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("invalid-isbn");
        bookDTO.setYear(2023);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> BookServiceHelper.validateRequestDTO(bookDTO));
        assertEquals("Invalid ISBN format. Must be 10-17 digits with optional hyphens", exception.getMessage());
    }

    @Test
    void testValidateRequestDTO_InvalidYear() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("1234567890");
        bookDTO.setYear(1499);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> BookServiceHelper.validateRequestDTO(bookDTO));
        assertEquals("Publication year cannot be before 1500", exception.getMessage());
    }

    @Test
    void testCreateBookFromDTO() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("1234567890");
        bookDTO.setTitle("Test Book");
        bookDTO.setYear(2023);
        bookDTO.setPrice(49.99);
        bookDTO.setGenre("Fiction");

        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        Book book = BookServiceHelper.createBookFromDTO(bookDTO, List.of(author));

        assertEquals("1234567890", book.getIsbn());
        assertEquals("Test Book", book.getTitle());
        assertEquals(2023, book.getYear());
        assertEquals(49.99, book.getPrice());
        assertEquals("Fiction", book.getGenre());
        assertEquals(Set.of(author), book.getAuthors());
    }

    @Test
    void testUpdateBookFromDTO() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Old Title");
        book.setYear(2020);
        book.setPrice(29.99);
        book.setGenre("Old Genre");

        BookUpdateDTO bookUpdateDTO = new BookUpdateDTO();
        bookUpdateDTO.setTitle("Updated Title");
        bookUpdateDTO.setYear(2023);
        bookUpdateDTO.setPrice(59.99);
        bookUpdateDTO.setGenre("Updated Genre");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(2L);
        updatedAuthor.setName("Updated Author");

        BookServiceHelper.updateBookFromDTO(book, bookUpdateDTO, List.of(updatedAuthor));

        assertEquals("Updated Title", book.getTitle());
        assertEquals(2023, book.getYear());
        assertEquals(59.99, book.getPrice());
        assertEquals("Updated Genre", book.getGenre());
        assertEquals(Set.of(updatedAuthor), book.getAuthors());
    }

    @Test
    void testHasMatchingAuthor() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author 1");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Author 2");

        Set<Author> bookAuthors = Set.of(author1, author2);

        Author searchAuthor = new Author();
        searchAuthor.setId(1L);
        searchAuthor.setName("Author 1");

        boolean result = BookServiceHelper.hasMatchingAuthor(bookAuthors, List.of(searchAuthor));
        assertTrue(result);

        Author nonMatchingAuthor = new Author();
        nonMatchingAuthor.setId(3L);
        nonMatchingAuthor.setName("Author 3");

        result = BookServiceHelper.hasMatchingAuthor(bookAuthors, List.of(nonMatchingAuthor));
        assertFalse(result);
    }
}
