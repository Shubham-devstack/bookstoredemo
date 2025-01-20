package com.assignment.bookstore.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

     @NotBlank(message = "Author name is required")
     private String name;

     private LocalDate birthday;
}