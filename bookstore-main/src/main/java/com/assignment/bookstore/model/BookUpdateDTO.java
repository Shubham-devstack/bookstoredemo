package com.assignment.bookstore.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDTO {

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Year is required")
    @Min(value = 1, message = "Year can not be less than 1")
    private int year;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price can not be less than 1")
    private double price;

    @NotNull(message = "Genre is required")
    private String genre;

    private List<AuthorDTO> authors;
}