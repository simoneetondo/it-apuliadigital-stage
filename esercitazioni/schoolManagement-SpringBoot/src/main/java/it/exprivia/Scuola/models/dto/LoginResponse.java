package it.exprivia.Scuola.models.dto;

// si utilizzano i record solitamente per le classi DTO 
// un messaggio di risposta
// il ruolo
// e person che possiamo decidere student o teacher
public record LoginResponse(
        String message,
        String role,
        String token,
        PersonDTO user) {
}
