package com.example.LibraryManager.repositories;

import com.example.LibraryManager.TestUtil;
import com.example.LibraryManager.models.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @BeforeEach
    void setMock(){
        authorRepository.deleteAll();
    }

    @Test
    public void testFindById_ShouldPass() {
        // Arrange
        Author author = TestUtil.getAuthor("Author 1");
        authorRepository.save(author);

        // Act
        Optional<Author> savedAuthor = authorRepository.findById(author.getId());

        // Assert
        assertTrue(savedAuthor.isPresent());
        assertEquals(author, savedAuthor.get());
        assertEquals(author.getName(), savedAuthor.get().getName());
    }

    @Test
    public void testFindAll_ShouldPass() {
        // Arrange
        Author author1 = TestUtil.getAuthor(1L, "Author 1");
        Author author2 = TestUtil.getAuthor(2L, "Author 2");
        authorRepository.saveAll(List.of(author1, author2));

        // Act
        List<Author> authors = authorRepository.findAll();

        // Assert
        assertEquals(2, authors.size());

        List<Author> authorsContainingAuthor1 = authors.stream()
                .filter(author -> author1.getId().equals(author.getId()) && author1.getName().equals(author.getName()))
                .toList();
        assertEquals(1, authorsContainingAuthor1.size());

        List<Author> authorsContainingAuthor2 = authors.stream()
                .filter(author -> author2.getId().equals(author.getId()) && author2.getName().equals(author.getName()))
                .toList();
        assertEquals(1, authorsContainingAuthor2.size());
    }
}
