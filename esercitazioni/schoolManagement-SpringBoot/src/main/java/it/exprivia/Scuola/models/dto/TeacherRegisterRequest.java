package it.exprivia.Scuola.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TeacherRegisterRequest(

        @Email(message = "Inserisci un indirizzo email valido.")
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

        @Schema(description = "Nome completo dell'utente", example = "Mario ")
        @NotBlank(message = "Il campo è obbligatorio")
        @Size(min=2, max=30, message="Il nome deve essere lungo almeno 2 caratteri e massimo 30.")
        String firstName,

        @NotBlank(message = "Il campo è obbligatorio")
        @Size(min=2, max=30, message="Il cognome deve essere lungo almeno 2 caratteri e massimo 30.")
        String lastName,

        @NotBlank(message="Il campo è obbligatorio.")
        String teacherSub) {

}
