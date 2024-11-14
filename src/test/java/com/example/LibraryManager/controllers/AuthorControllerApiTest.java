package com.example.LibraryManager.controllers;

import com.example.LibraryManager.models.dto.EditAuthor;
import com.example.LibraryManager.models.entity.Author;
import com.example.LibraryManager.repositories.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthorRepository authorRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private final String authorsUrl = "/api/authors";

    @BeforeEach
    void setMock(){
        authorRepository.deleteAll();
    }

    @Test
    public void createAuthor_ShouldPass() throws Exception {

        EditAuthor editAuthor = new EditAuthor();
        editAuthor.setName("Author Name");
        String content = mapper.writeValueAsString(editAuthor);

        mockMvc.perform(post(authorsUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assert authorRepository.count() == 1;
    }

    @Test
    public void updateAuthor_ShouldPass() throws Exception {

        Author author = new Author();
        author.setName("Author Name");
        authorRepository.save(author);

        EditAuthor editAuthor = new EditAuthor();
        editAuthor.setName("Updated Name");
        String content = mapper.writeValueAsString(editAuthor);

        mockMvc.perform(put(authorsUrl + "/" + author.getId())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assert authorRepository.count() == 1;
        assert editAuthor.getName().equals(authorRepository.findById(author.getId()).orElse(new Author()).getName());
    }

    @Test
    public void getAll_ShouldPass() throws Exception {

        Author author = new Author();
        author.setName("Author Name");
        Author author2 = new Author();
        author2.setName("Author Name 2");
        authorRepository.saveAll(List.of(author, author2));

        mockMvc.perform(get(authorsUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAuthorById_ShouldPass() throws Exception {

        Author author = new Author();
        author.setName("Author Name");
        authorRepository.save(author);

        mockMvc.perform(get(authorsUrl + "/" + author.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.name").value(author.getName()));
    }

    @Test
    public void deleteById_ShouldPass() throws Exception {

        Author author = new Author();
        author.setName("Author Name");
        authorRepository.save(author);
        assert authorRepository.count() == 1;

        mockMvc.perform(delete(authorsUrl + "/" + author.getId()))
                .andExpect(status().isNoContent());

        assert authorRepository.count() == 0;
    }

    @Test
    public void createAuthor_UniqueDataIntegrityException() throws Exception {

        Author author = new Author();
        author.setName("Author Name");
        authorRepository.save(author);
        String content = mapper.writeValueAsString(author);

        mockMvc.perform(post(authorsUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error")
                        .value("A unique constraint violation occurred. Please ensure the data is unique."));

        assert authorRepository.count() == 1;
    }

    @Test
    public void createAuthor_MethodArgumentNotValidException() throws Exception {

        Author author = new Author();
        String content = mapper.writeValueAsString(author);

        mockMvc.perform(post(authorsUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("An error occurred. Please try again later. "))
                .andExpect(jsonPath("$.name").value("must not be blank"));

        assert authorRepository.count() == 0;
    }

    @Test
    public void updateAuthor_NotFoundException() throws Exception {

        Author author = new Author();
        author.setName("Author Name");
        String content = mapper.writeValueAsString(author);

        mockMvc.perform(put(authorsUrl + "/" + 10)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("author not found"));
    }

    @Test
    public void getAuthorById_NotFoundException() throws Exception {

        mockMvc.perform(get(authorsUrl + "/" + 10))
                .andExpect(status().isNotFound());
    }
}
