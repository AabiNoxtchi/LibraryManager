package com.example.LibraryManager.services;

import com.example.LibraryManager.TestUtil;
import com.example.LibraryManager.models.dto.EditAuthor;
import com.example.LibraryManager.models.entity.Author;
import com.example.LibraryManager.repositories.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Long id = 1L;

    @Test
    public void testCreateNew_ShouldPass() {
        // Arrange
        when(authorRepository.save(any())).thenReturn(TestUtil.getAuthor(id, "Author Name"));
        EditAuthor editAuthor = new EditAuthor();
        editAuthor.setName("Author Name");

        // Act
        Author author = authorService.add(editAuthor);

        // Assert
        verify(authorRepository, times(1)).save(any());
        assertEquals(id, author.getId());
        assertEquals("Author Name", author.getName());
    }

    @Test
    public void testUpdate_ShouldPass() {
        // Arrange
        when(authorRepository.findById(id)).thenReturn(Optional.of(TestUtil.getAuthor(id, "Author Name")));
        when(authorRepository.save(any())).thenReturn(TestUtil.getAuthor(id, "Updated Name"));
        EditAuthor editAuthor = new EditAuthor();
        editAuthor.setName("Updated Name");

        // Act
        Author author = authorService.update(id, editAuthor);

        // Assert
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).save(any());
        assertEquals(id, author.getId());
        assertEquals("Updated Name", author.getName());
    }

    @Test
    public void testGetAll_ShouldPass() {
        // Arrange
        when(authorRepository.findAll()).thenReturn(TestUtil.getAuthoursList(2));

        // Act
        List<Author> authors = authorService.getAll();

        // Assert
        verify(authorRepository, times(1)).findAll();
        assertEquals(2, authors.size());
    }

    @Test
    public void testGetById_ShouldPass() {
        // Arrange
        when(authorRepository.findById(id)).thenReturn(Optional.of(TestUtil.getAuthor(id, "Author Name")));

        // Act
        Optional<Author> author = authorService.getById(id);

        // Assert
        verify(authorRepository, times(1)).findById(id);
        assertTrue(author.isPresent());
    }

    @Test
    public void testDelete_ShouldPass() {
        // Act
        authorService.delete(id);

        // Assert
        verify(authorRepository, times(1)).deleteById(id);
    }

}
