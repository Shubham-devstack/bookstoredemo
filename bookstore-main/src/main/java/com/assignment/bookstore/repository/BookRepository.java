package com.assignment.bookstore.repository;

import com.assignment.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(String title, String author);
}