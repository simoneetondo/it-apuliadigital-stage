package it.exprivia.exception.book;

import it.exprivia.exception.ServiceException;

public class DuplicateBookException extends ServiceException {
    public DuplicateBookException(String isbn) {
        super("Book with ISBN " + isbn + " already exists.", 400);
    }
}
