package com.example.LibraryManager.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class EditBook {

    @NotBlank
    private String title;

    @NotBlank
    private String isbn;

    @NotEmpty
    private Set<Long> authorIds;

}
