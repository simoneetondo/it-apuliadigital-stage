package it.exprivia.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        // rimuoviamo trattini o spazi per la validazione
        String cleanIsbn = value.replace("-", "");

        // ccapiamo se è un ISBN-10 o ISBN-13
        return cleanIsbn.length() == 13 || cleanIsbn.length() == 10;
    }
}