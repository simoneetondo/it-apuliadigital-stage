package it.exprivia.exception;

// serviceException utile a dare una struttura a tutte le eccezioni che vogliamo lanciare, in questo modo le gestiamo
// tutte in un unico punto, ovvero il GlobalHandler, che è un ExceptionMapper che intercetta tutte le eccezioni
// di tipo ServiceException e restituisce una risposta HTTP con lo status code e il messaggio dell'eccezione.
public abstract class ServiceException extends RuntimeException {
    private final int statusCode;

    public ServiceException(String message, int StatusCode) {
        super(message);
        this.statusCode = StatusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
