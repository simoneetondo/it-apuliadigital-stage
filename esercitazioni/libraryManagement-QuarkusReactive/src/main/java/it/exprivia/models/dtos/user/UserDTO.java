package it.exprivia.models.dtos.user;

import com.google.errorprone.annotations.FormatMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.jboss.resteasy.reactive.DateFormat;

import java.time.LocalDate;
import java.util.Date;

public record UserDTO(
        @NotBlank(message = "Codice fiscale must not be blank")
        //INSERIRE REJEX O CUSTOM VALIDATION
        String codiceFiscale,
        @NotBlank(message = "Name must not be blank")
        String firstName,
        @NotBlank(message = "Surname must not be blank")
        String lastName,
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Phone number must not be blank")
        //INSERIRE REJEX O CUSTOM VALIDATION
        String phoneNumber,
        @NotBlank(message = "Address must not be blank")
        Date dateOfBirth
) {
}
