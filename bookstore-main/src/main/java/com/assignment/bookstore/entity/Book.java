/**
 * Provides getter and setter methods for the properties of a Book entity.
 */
package com.assignment.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    private String isbn;

    private String title;
    private Integer year;
    private Double price;
    private String genre;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_isbn"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;
}
