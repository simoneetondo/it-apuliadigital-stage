package it.exprivia.Scuola.models.dto;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

// import java.sql.Date;

// import com.fasterxml.jackson.annotation.JsonFormat;
// import com.fasterxml.jackson.annotation.JsonIgnore;

// import lombok.AllArgsConstructor;
// import lombok.EqualsAndHashCode;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// DTO = DATA TRANSFER OBJECT non vogliamo toccare il db 
// si usano per fare operazioni/controlli/modifiche senza toccare le entità
// con le entità si interfaccia solamente il repository

// provando ad utilizzare lombock 

// @NoArgsConstructor
// @AllArgsConstructor
// @Getter
// @Setter
// @EqualsAndHashCode(exclude = {"username", "password"})

public record StudentDTO(
        Integer id,
        @Email
        String email,
        String username,
        String firstName,
        String lastName,
        String stuNum,
        LocalDate dateBr)
        implements PersonDTO {
}

// valgono solo per le entità non per i dto
// @Id
// @GeneratedValue
// // Integer id;
// @JsonIgnore
// private String username;
// @JsonIgnore
// private String password;
// private String firstName;
// private String lastName;
// private String stuNum;
// // metodo per formattare la data levando orario
// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
// private Date dateBr;

// public StudentDTO(String firstName, String lastName, String stuNum){
// this.firstName= firstName;
// this.lastName = lastName;
// this.stuNum = stuNum;
// }

// public record StudentDTO ( * PARAMETRI *)
