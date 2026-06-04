package it.exprivia.Scuola.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// puo essere considerato un aspetto, intercetta le chiamate effettuate da qualunque controller
// @ControllerAdvice è un aspetto specializzato, sa quando intervenire, u
// sa la reflection per trovare i metodi  annotati con @ExceptionHandler dentro la classe advice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        // prende il messaggio che abbiamo messo nel service più lo stato 404, cosi' nel
        // controller
        // inseriamo solamente la logica felice nel caso in cui dovesse andare tutto a
        // buon fine
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // SOLITAMENTE IL CONTROLLER ADVICE VIENE CONFIGURATO PER GESTIRE UN PROBLEM
    // DETAIL CHE IMPLEMENTANO LO STANDARD RFC 7008 PER TUTTE LE AZIENDE,
    // UN JSON COMUNE A TUTTI

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResourceException(DuplicateResourceException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        pd.setTitle("Duplicated Resource");
        pd.setProperty("errorCode", "DUPLICATE_ERROR");


        return pd;
    }

    // viene spesso usato in ambito aziendale per gestire i problemi generici,
    // quelli non previsti
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Si è verificato un errore inaspettato sul server.");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "I dati inseriti non sono validi.");
        pd.setTitle("Errore di validazione");

        Map<String, String> errors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (existing, replacement) -> existing + " e " + replacement
                ));
        pd.setProperty("invalidFields", errors);

        return pd;
    }

    // eccezione per il login fallito
    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        pd.setTitle("Autenticazione fallita");
        return pd;
    }

}


//          estrarre la lista degli errori nei campi
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            // field: il nome del campo (es. "password")
//            // defaultMessage: il messaggio che abbiamo scritto nel DTO
//            errors.put(error.getField(), error.getDefaultMessage());


//     Map<String, String> errors = ex.getBindingResult()
//             .getFieldErrors()
//             .stream()
//             .collect(Collectors.toMap(
//                     FieldError::getField,           // Chiave: nome del campo
//                     FieldError::getDefaultMessage,  // Valore: messaggio di errore
//                     (existing, replacement) -> existing + " e " + replacement // Unisce più messaggi per lo stesso campo
//             ));

//     Aggiungiamo la mappa al JSON di risposta
//     pd.setProperty("errors", errors);