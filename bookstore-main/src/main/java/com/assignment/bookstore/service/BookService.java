
package com.assignment.bookstore.service;

import com.assignment.bookstore.entity.Author;
import com.assignment.bookstore.entity.Book;
import com.assignment.bookstore.exception.EntityExistsException;
import com.assignment.bookstore.exception.BookNotFoundException;
import com.assignment.bookstore.exception.InvalidInputException;
import com.assignment.bookstore.model.AuthorDTO;
import com.assignment.bookstore.model.BookDTO;
import com.assignment.bookstore.model.BookUpdateDTO;
import com.assignment.bookstore.repository.BookRepository;
import com.assignment.bookstore.helper.BookServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 * Service class for managing book-related operations.
 */

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }


    /**
     * Creates a new book in the system.
     */

    @Transactional
    public Book addBook(BookDTO bookDTO) {
        try {
            log.info("Adding new book with ISBN: {}", bookDTO.getIsbn());
            BookServiceHelper.validateRequestDTO(bookDTO);
            verifyIsbnDoesNotExist(bookDTO.getIsbn());
            List<Author> authors = authorService.createAuthors(bookDTO.getAuthors());
            Book book = BookServiceHelper.createBookFromDTO(bookDTO, authors);
            return bookRepository.save(book);
        } catch (Exception e) {
            log.error("Error adding book ", e);
            throw e;
        }
    }

    @Transactional
    public Book updateBook(String isbn, BookUpdateDTO bookUpdateDTO) {
        try {
            log.info("Updating book with ISBN: {}", isbn);
            Book existingBook = verifyAndGetBook(isbn);
            List<Author> authors = getAuthorsForUpdate(bookUpdateDTO.getAuthors());
            BookServiceHelper.updateBookFromDTO(existingBook, bookUpdateDTO, authors);
            return bookRepository.save(existingBook);
        }
        catch (Exception e) {
            log.error("Error updating book ", e);
            throw e;
        }
    }


    /**
     * Deletes a book by its ISBN.
     */

    @Transactional
    public void deleteBook(String isbn) {
        try {
            log.info("Deleting book with ISBN: {}", isbn);
            verifyAndGetBook(isbn);
            bookRepository.deleteById(isbn);
        }
        catch (Exception e) {
            log.error("Error deleting book ", e);
            throw e;
        }
    }


    /**
     * Finds a book by its ISBN.
     */

    @Transactional(readOnly = true)
    public Book findBookByIsbn(String isbn) {
        log.info("Finding book with ISBN: {}", isbn);
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN: " + isbn + " not found"));
    }


    /**
     * Finds books by title and/or author name.
     */

    @Transactional(readOnly = true)
    public List<Book> findBooks(String title, String author) {
        log.info("Searching for books with title: {} and author: {}", title, author);
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(title, author);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found with title containing '" + title + "' and author containing '" + author + "'");
        }
        return books;
    }


    /**
      * Retrieves all books in the system.
      */

    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        log.info("Fetching all books");
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found in the system");
        }
        return books;
    }


    /**
     * Verifies if an ISBN already exists in the system.
     */

    private void verifyIsbnDoesNotExist(String isbn) {
        if (bookRepository.findById(isbn).isPresent()) {
            log.error("Book with ISBN {} already exists", isbn);
            throw new EntityExistsException("Book with ISBN " + isbn + " already exists.");
        }
    }


    /**
     * Retrieves an author by name.
     */

    private List<Author> getAuthorsForUpdate(List<AuthorDTO> authorNames) {
        return authorNames != null ? authorService.createAuthors(authorNames) : null;
    }


    /**
     * Verifies the existence of a book by ISBN and returns it.
     */

    private Book verifyAndGetBook(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN: " + isbn + " not found"));
    }
}

