package com.example.LibraryManager.models.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    @Column(unique = true)
    private String isbn;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Author> authors = new HashSet<>();

    public void addAuthors(List<Author> authors) {
        this.authors.addAll(authors);
    }
}
