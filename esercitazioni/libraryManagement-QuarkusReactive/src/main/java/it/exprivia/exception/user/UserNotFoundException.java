package it.exprivia.exception.user;

import it.exprivia.exception.ServiceException;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String codiceFiscale) {
        super("User with codice fiscale " + codiceFiscale + " not found.", 404);
    }
}
