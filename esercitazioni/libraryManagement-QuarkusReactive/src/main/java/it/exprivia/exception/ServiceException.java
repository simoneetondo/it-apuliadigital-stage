package it.exprivia.exception;

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
