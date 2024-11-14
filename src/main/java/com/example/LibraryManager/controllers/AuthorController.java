package com.example.LibraryManager.controllers;


import com.example.LibraryManager.models.dto.EditAuthor;
import com.example.LibraryManager.models.entity.Author;
import com.example.LibraryManager.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public ResponseEntity<Author> add(@Valid @RequestBody EditAuthor editAuthor) {
        Author saved = service.add(editAuthor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Long id, @Valid @RequestBody EditAuthor editAuthor) {
        return ResponseEntity.ok(service.update(id, editAuthor));
    }


    @GetMapping
    public ResponseEntity<List<Author>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
