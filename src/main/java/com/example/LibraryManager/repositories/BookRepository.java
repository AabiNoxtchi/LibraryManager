package com.example.LibraryManager.repositories;

import com.example.LibraryManager.models.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
