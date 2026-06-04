package it.exprivia.exception.user;

import it.exprivia.exception.ServiceException;

public class DuplicateUserException extends ServiceException {
    public DuplicateUserException(String codiceFiscale) {
        super("User with codice fiscale " + codiceFiscale + " already exists.", 400);
    }
}
