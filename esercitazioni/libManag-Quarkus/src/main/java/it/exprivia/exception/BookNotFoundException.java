package it.exprivia.exception;

public class BookNotFoundException extends ServiceException {
    public BookNotFoundException(String isbn) {
        super("Book with ISBN: " + isbn + " not found.", 404);
    }
}
