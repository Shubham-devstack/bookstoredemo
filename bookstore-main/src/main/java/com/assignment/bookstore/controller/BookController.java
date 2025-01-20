package com.assignment.bookstore.controller;

import com.assignment.bookstore.entity.Book;
import com.assignment.bookstore.model.BookDTO;
import com.assignment.bookstore.model.BookUpdateDTO;
import com.assignment.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
@Slf4j
@Tag(name = "Book Management", description = "APIs for managing books in the bookstore")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Create a new book",
            description = "Creates a new book entry with authors in the system")
    @ApiResponse(responseCode = "201", description = "Book successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping("/")
    public ResponseEntity<Book> createBookCatalogEntry(@Valid @RequestBody BookDTO bookDTO) {
        log.info("Creating new catalog entry for book: {}", bookDTO.getTitle());
        Book addedBook = bookService.addBook(bookDTO);
        log.info("Successfully created catalog entry with ISBN: {}", addedBook.getIsbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    @Operation(summary = "Update book details",
            description = "Updates an existing book's information and its authors")
    @ApiResponse(responseCode = "200", description = "Book successfully updated")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBookCatalogEntry(
            @Parameter(description = "ISBN of the book to update") @PathVariable String isbn,
            @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        log.info("Updating catalog entry for book with ISBN: {}", isbn);
        Book updatedBook = bookService.updateBook(isbn, bookUpdateDTO);
        log.info("Successfully updated catalog entry with ISBN: {}", isbn);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Search books by criteria",
            description = "Search books by title and/or author name")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBookCatalog(
            @Parameter(description = "Book title to search for") @RequestParam(required = false) String title,
            @Parameter(description = "Author name to search for") @RequestParam(required = false) String author) {
        log.debug("Searching catalog with title: {} and author: {}", title, author);
        List<Book> books = bookService.findBooks(title, author);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get book catalog",
            description = "Retrieves the complete book catalog")
    @ApiResponse(responseCode = "200", description = "Catalog retrieved successfully")
    @GetMapping("/")
    public ResponseEntity<List<Book>> getBookCatalog() {
        log.debug("Retrieving complete book catalog");
        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get book details by ISBN",
            description = "Retrieves detailed information about a specific book")
    @ApiResponse(responseCode = "200", description = "Book details found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookDetails(
            @Parameter(description = "ISBN of the book") @PathVariable String isbn) {
        log.debug("Retrieving book details for ISBN: {}", isbn);
        Book book = bookService.findBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Remove book from catalog",
            description = "Permanently removes a book from the catalog. Requires admin privileges.")
    @ApiResponse(responseCode = "204", description = "Book successfully removed")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @DeleteMapping("/{isbn}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> removeBookFromCatalog(
            @Parameter(description = "ISBN of the book to remove") @PathVariable String isbn) {
        log.info("Removing book from catalog with ISBN: {}", isbn);
        bookService.deleteBook(isbn);
        log.info("Successfully removed book with ISBN: {}", isbn);
        return ResponseEntity.noContent().build();
    }
}