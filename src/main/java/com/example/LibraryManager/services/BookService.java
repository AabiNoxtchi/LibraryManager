package com.example.LibraryManager.services;

import com.example.LibraryManager.models.dto.EditBook;
import com.example.LibraryManager.models.entity.Author;
import com.example.LibraryManager.models.entity.Book;
import com.example.LibraryManager.repositories.AuthorRepository;
import com.example.LibraryManager.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repo;
    private final AuthorRepository authorRepo;


    public Book add(EditBook editBook) {

        Set<Author> authorSet = new HashSet<>(authorRepo.findAllById(editBook.getAuthorIds()));

        Book book = Book.builder()
                .title(editBook.getTitle())
                .isbn(editBook.getIsbn())
                .authors(authorSet).build();

        return repo.save(book);
    }

    public Book update(Long id, EditBook editBook) {
        Book book = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        book.setTitle(editBook.getTitle());
        book.setIsbn(editBook.getIsbn());

        List<Long> existingIds = book.getAuthors().stream().map(Author::getId).toList();
        List<Long> authorIdsToAdd =
                editBook.getAuthorIds().stream()
                        .filter(authorId -> !existingIds.contains(authorId))
                        .toList();
        List<Author> authors = authorRepo.findAllById(authorIdsToAdd);

        book.getAuthors().removeIf(author -> !editBook.getAuthorIds().contains(author.getId()));
        book.addAuthors(authors);

        return repo.save(book);
    }

    public List<Book> getAll() {
        return repo.findAll();
    }

    public Optional<Book> getById(Long id) {
        return repo.findById(id);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}
