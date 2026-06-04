package it.exprivia.Scuola.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.exprivia.Scuola.models.dto.StudentDTO;
import it.exprivia.Scuola.models.dto.StudentRegisterRequest;
import it.exprivia.Scuola.services.IStudent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/students")
@Tag(name = "studenti", description = "Operazioni relative agli studenti")
public class StudentController {

    @Autowired
    private IStudent service;

    private Logger logger;

    // inserire un oggetto di tipo DTO e mapparlo nel service

    // GET ALL

    // sono tutte annotaion che servono per documentare le api con swagger
    @Operation(
            summary = "Recupera tutti gli studenti",
            description = "Recupera la lista di tutti gli studenti presenti nel sistema"
    )
    @ApiResponse(responseCode = "200", description = "Elenco degli studenti recuperato con successo")
    @ApiResponse(responseCode = "403", description = "Accesso negato")
    // inserire @operation e studiare sweagger
    @GetMapping("")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<StudentDTO>> getStudents() {
        return ResponseEntity.ok(service.getStudents());

    }

    @Operation(
            summary = "Recupera uno studente per ID",
            description = "Recupera i dettagli di uno studente specifico utilizzando il suo ID univoco"
    )
    @ApiResponse(responseCode = "200", description = "Studente recuperato con successo")
    @ApiResponse(responseCode = "404", description = "Studente non trovato")
    // GET BY ID
    @GetMapping("{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDTO> getStudent(
            @Parameter(description = "ID univoco dello studente", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(service.getStudent(id));
    }


    @Operation(
            summary = "Elimina uno studente per ID",
            description = "Elimina uno studente specifico utilizzando il suo ID univoco"
    )
    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Crea un nuovo studente",
            description = "Crea un nuovo studente nel sistema con i dettagli forniti"
    )

    // CREATE
    @PostMapping("")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentRegisterRequest stud) {
        StudentDTO createdStudent = service.saveStudent(stud);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Aggiorna uno studente esistente",
            description = "Aggiorna i dettagli di uno studente specifico utilizzando il suo ID univoco"
    )
    @ApiResponse(responseCode = "200", description = "Studente aggiornato con successo")
    @ApiResponse(responseCode = "404", description = "Studente non trovato")
    @ApiResponse(responseCode = "409", description = "Conflitto durante l'aggiornamento dello studente")
    // UPDATE 
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Integer id, @RequestBody StudentDTO newStudent) {
        StudentDTO modifiedStudent = service.updateStudent(id, newStudent);
        return new ResponseEntity<>(modifiedStudent, HttpStatus.OK);
    }


    @Operation(
            summary = "Recupera tutti gli studenti che hanno il nome filtrato",
            description = "Recupera la lista di tutti gli studenti presenti nel sistema in base al nome"
    )
    @ApiResponse(responseCode = "200", description = "Elenco degli studenti recuperato con successo")
    @ApiResponse(responseCode = "403", description = "Accesso negato")
    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(
            @RequestParam String firstName) {
        return ResponseEntity.ok(service.getStudentsByName(firstName));
    }

}