package com.example.LibraryManager.services;

import com.example.LibraryManager.models.dto.EditAuthor;
import com.example.LibraryManager.models.entity.Author;
import com.example.LibraryManager.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author add(EditAuthor editAuthor) {
        Author author = new Author();
        author.setName(editAuthor.getName());

        return authorRepository.save(author);
    }

    public Author update(Long id, EditAuthor editAuthor) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("author not found"));

        author.setName(editAuthor.getName());

        return authorRepository.save(author);
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Optional<Author> getById(Long id) {
        return authorRepository.findById(id);
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
