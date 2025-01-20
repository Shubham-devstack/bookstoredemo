package com.assignment.bookstore.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    @Test
    void testAuthorCreation() {
        Author author = new Author();
        assertNotNull(author);
    }

    @Test
    void testAuthorGettersAndSetters() {
        Author author = new Author();

        author.setId(1L);
        author.setName("Test Author");
        author.setBirthday(LocalDate.of(1990, 5, 20));

        assertEquals(1L, author.getId());
        assertEquals("Test Author", author.getName());
        assertEquals(LocalDate.of(1990, 5, 20), author.getBirthday());
    }

    @Test
    void testDefaultValues() {
        Author author = new Author();

        assertNull(author.getId());
        assertNull(author.getName());
        assertNull(author.getBirthday());
    }

    @Test
    void testInvalidValues() {
        Author author = new Author();

        author.setName("");
        author.setBirthday(null);

        assertEquals("", author.getName());
        assertNull(author.getBirthday());
    }
}
