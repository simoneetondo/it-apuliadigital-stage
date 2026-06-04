package it.exprivia.exception.book;

import it.exprivia.exception.ServiceException;

/**
 * serviceException utile a dare una struttura a tutte le eccezioni che vogliamo lanciare, in questo modo le gestiamo
 * tutte in un unico punto, ovvero il GlobalHandler, che è un ExceptionMapper che intercetta tutte le eccezioni
 * di tipo ServiceException e restituisce una risposta HTTP con lo status code e il messaggio dell'eccezione.
 **/
public class BookNotFoundException extends ServiceException {
    public BookNotFoundException(String isbn) {
        super("Book with ISBN: " + isbn + " not found.", 404);
    }
}
