package com.example.LibraryManager;

import com.example.LibraryManager.models.entity.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtil {

    public static Author getAuthor(Long id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        return author;
    }

    public static List<Author> getAuthoursList(int size) {
        List<Author> authors = new ArrayList<>();
        IntStream.range(0, size).forEach(number -> {
            Author author = TestUtil.getAuthor((long) number + 1, "Author Name " + number);
            authors.add(author);
        });

        return authors;
    }

    public static Author getAuthor(String name) {
        Author author = new Author();
        author.setName(name);
        return author;
    }
}
