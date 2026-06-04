package it.exprivia.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// @Target utilizzato per indicare che @ValidIsbn può essere applicato sopra un campo (FIELD) o un parametro di metodo (PARAMETER).
// quindi in conclusione dove puoi usare l'annotation
@Target({ElementType.FIELD, ElementType.PARAMETER})

// @Retention indica che l'annotation è disponibile a runtime,
// quindi può essere letta tramite reflection durante l'esecuzione dell'applicazione
@Retention(RetentionPolicy.RUNTIME)

// @Constraint specifica che questa annotation è una constraint di validazione e specifica la classe che contiene
// la logica di validazione, in questo caso IsbnValidator
@Constraint(validatedBy = IsbnValidator.class) // Chi contiene la logica

// @Documenter serve ad inserire l'annotation nei javadoc per la generazione della documentazione
@Documented

public @interface ValidIsbn {

    // questi sono i tre metodi obbligatori ( STANDARD BEAN VALIDATION )
    // devono essere presenti in ogni annotation e servono per

    // 1. message: il messaggio di errore che verrà restituito se la validazione fallisce

    // 2. groups: permette di raggruppare più constraint insieme, in questo modo puoi applicare diverse validazioni
    // a seconda del contesto (es. validazione per la creazione vs validazione per l'aggiornamento)

    // 3. payload: permette di associare informazioni aggiuntive alla constraint, ad esempio un codice di errore personalizzato

    String message() default "ISBN non valido secondo lo standard internazionale";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}