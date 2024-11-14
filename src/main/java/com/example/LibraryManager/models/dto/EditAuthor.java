package com.example.LibraryManager.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditAuthor {

    @NotBlank
    private String name;

}
