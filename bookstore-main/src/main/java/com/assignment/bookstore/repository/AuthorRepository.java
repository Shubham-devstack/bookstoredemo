package com.assignment.bookstore.repository;

import com.assignment.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameIgnoreCaseContaining(String name);
}