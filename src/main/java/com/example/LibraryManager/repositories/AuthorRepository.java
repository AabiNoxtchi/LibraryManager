package com.example.LibraryManager.repositories;

import com.example.LibraryManager.models.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {}

