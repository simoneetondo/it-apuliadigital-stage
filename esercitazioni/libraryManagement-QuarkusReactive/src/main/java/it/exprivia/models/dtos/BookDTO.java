package it.exprivia.models.dtos;

import java.util.List;

public record BookDTO(
        String isbn,
        String title,
        String author,
        int publicationYear,
        List<String> genre
) {
}
