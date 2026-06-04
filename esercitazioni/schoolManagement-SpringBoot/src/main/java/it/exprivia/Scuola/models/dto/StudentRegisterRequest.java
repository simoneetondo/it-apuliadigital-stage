package it.exprivia.Scuola.models.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

public record StudentRegisterRequest(

        @Email(message = " Inserisci un indirizzo email valido.")
        @NotBlank(message = "Il campo è obbligatorio.")
        String email,

        @NotBlank(message = "Il campo è obbligatorio.")
        String username,

        @NotBlank(message = "Il campo è obbligatorio")
        @Size(min = 8, max = 18, message = "La password deve contentere almeno 8 caratteri.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "La password deve contenere almeno una lettera maiuscola, una minuscola e un numero"
        )
        String password,

        @NotBlank(message = "Il campo è obbligatorio.")
        String firstName,

        @NotBlank(message = "Il campo è obbligatorio.")
        String lastName,

        @NotBlank(message = "Il campo è obbligatorio.")
        String stuNum,

        @NotNull(message = "Il campo è obbligatorio.")
        LocalDate dateBr

        // gestire eventuali problemi di validazione con i problem detail.


) {
}
