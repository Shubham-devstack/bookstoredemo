package com.assignment.bookstore.exception;

public class BookNotFoundException extends RuntimeException {
     public BookNotFoundException(String message) {
         super(message);
     }
}