package com.assignment.bookstore.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

     @NotBlank(message = "ISBN is required")
     private String isbn;

     @NotBlank(message = "Title is required")
     private String title;

     @NotNull(message = "Year is required")
     @Min(value = 1, message = "Year can not be less than 1")
     private int year;

     @NotNull(message = "Price is required")
     @Min(value = 1, message = "Price can not be less than 1")
     private double price;

     @NotBlank(message = "Genre is required")
     private String genre;

     @NotNull(message = "Authors are required")
     private List<AuthorDTO> authors;
 }