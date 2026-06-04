package it.exprivia.exception.book;

import it.exprivia.exception.ServiceException;

public class InsufficientStockException extends ServiceException {
    public InsufficientStockException(String isbn) {
        super("Quantity requested for book with ISBN: " + isbn + " exceeds available stock.", 400);
    }
}
