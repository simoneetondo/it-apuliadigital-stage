package it.exprivia.exception;

public class DuplicatedBookException extends ServiceException {
    public DuplicatedBookException(String isbn) {

        super("Book already exists with ISBN: " + isbn + ".", 409);
    }
}
