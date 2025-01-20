package com.assignment.bookstore.common;

import com.assignment.bookstore.exception.BookNotFoundException;
import com.assignment.bookstore.exception.EntityExistsException;
import com.assignment.bookstore.exception.InvalidInputException;
import com.assignment.bookstore.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleBookNotFoundException() {
        BookNotFoundException ex = new BookNotFoundException("Book not found");
        ResponseEntity<Object> response = exceptionHandler.handleBookNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertResponseBody(response.getBody(), HttpStatus.NOT_FOUND.value(), "Not Found", "Book not found");
    }

    @Test
    void testHandleEntityExistsException() {
        EntityExistsException ex = new EntityExistsException("Entity already exists");
        ResponseEntity<Object> response = exceptionHandler.handleEntityExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertResponseBody(response.getBody(), HttpStatus.CONFLICT.value(), "Conflict", "Entity already exists");
    }

    @Test
    void testHandleUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized access");
        ResponseEntity<Object> response = exceptionHandler.handleUnauthorizedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertResponseBody(response.getBody(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Unauthorized access");
    }

    @Test
    void testHandleInvalidInputException() {
        InvalidInputException ex = new InvalidInputException("Invalid input provided");
        ResponseEntity<Object> response = exceptionHandler.handleInvalidInputException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertResponseBody(response.getBody(), HttpStatus.BAD_REQUEST.value(), "Bad Request", "Invalid input provided");
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("Some unexpected error");
        ResponseEntity<Object> response = exceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertResponseBody(response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "An unexpected error occurred");
    }

    private void assertResponseBody(Object body, int status, String error, String message) {
        assertInstanceOf(Map.class, body);
        Map<String, Object> responseBody = (Map<String, Object>) body;

        assertNotNull(responseBody.get("timestamp"));
        assertInstanceOf(Date.class, responseBody.get("timestamp"));
        assertEquals(status, responseBody.get("status"));
        assertEquals(error, responseBody.get("error"));
        assertEquals(message, responseBody.get("message"));
    }
}