package com.assignment.bookstore.helper;

import com.assignment.bookstore.entity.Author;
import com.assignment.bookstore.entity.Book;
import com.assignment.bookstore.exception.InvalidInputException;
import com.assignment.bookstore.model.BookDTO;
import com.assignment.bookstore.model.BookUpdateDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Helper class containing utility methods for BookService operations.
 * Focuses on business-specific validations and transformations.
 */
public class BookServiceHelper {

    BookServiceHelper() {}

    /**
     * Performs business-specific validations for BookDTO.
     * Note: Basic field validations are handled by Jakarta validation annotations.
     */
    public static void validateRequestDTO(BookDTO bookDTO) {
        validateISBNFormat(bookDTO.getIsbn());
        validateYearRange(bookDTO.getYear());
    }

    private static void validateISBNFormat(String isbn) {
        if (!isbn.matches("^[0-9-]{10,17}$")) {
            throw new InvalidInputException("Invalid ISBN format. Must be 10-17 digits with optional hyphens");
        }
    }

    private static void validateYearRange(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year > currentYear + 2) {
            throw new InvalidInputException("Publication year cannot be more than 2 years in the future");
        }
        if (year < 1500) {
            throw new InvalidInputException("Publication year cannot be before 1500");
        }
    }


    public static Book createBookFromDTO(BookDTO bookDTO, List<Author> authors) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setYear(bookDTO.getYear());
        book.setPrice(bookDTO.getPrice());
        book.setGenre(bookDTO.getGenre());
        book.setAuthors(new HashSet<>(authors));
        return book;
    }

    public static void updateBookFromDTO(Book book, BookUpdateDTO bookUpdateDTO,List<Author> authors) {
        book.setTitle(bookUpdateDTO.getTitle());
        book.setYear(bookUpdateDTO.getYear());
        book.setPrice(bookUpdateDTO.getPrice());
        book.setGenre(bookUpdateDTO.getGenre());
        book.setAuthors(new HashSet<>(authors));
    }

    public static boolean hasMatchingAuthor(Set<Author> bookAuthors, List<Author> searchAuthors) {
        return bookAuthors.stream()
                .anyMatch(author -> searchAuthors.stream()
                        .anyMatch(a -> Objects.equals(a.getId(), author.getId())));
    }
}