package com.assignment.bookstore.service;

import com.assignment.bookstore.entity.Author;
import com.assignment.bookstore.model.AuthorDTO;
import com.assignment.bookstore.repository.AuthorRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author createAuthor(AuthorDTO authorDTO){
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setBirthday(authorDTO.getBirthday());
        return authorRepository.save(author);
    }

    public List<Author> findByAuthorName(String name) {
        return authorRepository.findByNameIgnoreCaseContaining(name);
    }

    public Author findAuthorById(long authorId){
        return authorRepository.findById(authorId).orElse(null);
    }

    public List<Author> createAuthors(List<AuthorDTO> authorDTOS){
        List<Author> authors = new ArrayList<>();
        for (AuthorDTO authorDTO : authorDTOS){
            authors.add(createAuthor(authorDTO));
        }
        return  authors;
    }


    public List<Author> findAll(){
        return  authorRepository.findAll();
    }
}