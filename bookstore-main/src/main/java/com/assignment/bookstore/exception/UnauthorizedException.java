package com.assignment.bookstore.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
    super(message);
}
}