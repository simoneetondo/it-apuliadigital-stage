package it.exprivia.models.dtos;

public record BookDTO(
        String isbn,
        String title,
        String author,
        int publicationYear,
        String genre,
        Integer stock
) {


}
