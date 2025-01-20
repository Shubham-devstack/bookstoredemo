package com.assignment.bookstore.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreation() {
        Book book = new Book();
        assertNotNull(book);
    }

    @Test
    void testBookGettersAndSetters() {
        Author author = new Author();
        author.setName("Test Author");
        author.setId(1L);

        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book.setAuthors(Set.of(author));
        book.setPrice(29.99);
        book.setGenre("Fiction");

        assertEquals("1234567890", book.getIsbn());
        assertEquals("Test Book", book.getTitle());
        assertEquals(Set.of(author), book.getAuthors());
        assertEquals(29.99, book.getPrice());
        assertEquals("Fiction", book.getGenre());
    }

    @Test
    void testNullValues() {
        Book book = new Book();

        assertNull(book.getIsbn());
        assertNull(book.getTitle());
        assertNull(book.getAuthors());
        assertNull(book.getPrice());
        assertNull(book.getGenre());
    }

    @Test
    void testPriceValues() {
        Book book = new Book();

        book.setPrice(0.0);
        assertEquals(0.0, book.getPrice());

        book.setPrice(-1.0);
        assertEquals(-1.0, book.getPrice());
    }
}