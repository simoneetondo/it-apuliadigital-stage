package it.exprivia.models.dtos.book;

import it.exprivia.validation.ValidIsbn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookCreateRequest(

        @ValidIsbn
        String isbn,

        @NotBlank(message = "Title must not be blank")
        String title,

        @NotBlank(message= "Author must not be blank")
        String author,

        @NotEmpty(message = "Genre cannot be empty")
        @Size(min = 1, message = "At least one genre must be provided")
        List<String> genre,

        @NotNull(message = "Publication year is required")
        Integer publicationYear,

        @NotEmpty(message = "Stock must not be empty")
        Integer stock


) {
}
