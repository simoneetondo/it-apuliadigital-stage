package it.exprivia.Scuola.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.exprivia.Scuola.models.dto.TeacherRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.exprivia.Scuola.models.dto.TeacherDTO;
import it.exprivia.Scuola.services.impl.TeacherServiceImpl;

@RestController
@RequestMapping("api/teachers")
@Tag(name="docenti", description="Operazioni relative agli insegnanti")
public class TeacherController {

    @Autowired
    private TeacherServiceImpl service;

    @Operation(
            summary = "Recupera tutti gli insegnanti",
            description = "Recupera la lista di tutti gli insegnanti presenti nel sistema"
    )
    @ApiResponse(responseCode = "200", description = "Elenco degli insegnanti recuperato con successo")
    @ApiResponse(responseCode = "403", description = "Accesso negato")
    @GetMapping("")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        // List<TeacherDTO> teachers = service.getAllTeachers();
        // if (teachers == null || teachers.isEmpty()) {
        // return ResponseEntity.noContent().build();
        // }
        return ResponseEntity.ok(service.getAllTeachers());
    }

    @Operation(
            summary = "Recupera un insegnante per ID",
            description = "Recupera i dettagli di un insegnante specifico utilizzando il suo ID univoco"
    )
    @ApiResponse(responseCode = "200", description = "Insegnante recuperato")
    @ApiResponse(responseCode = "404", description = "Insegnante non trovato")
    @GetMapping("/{varId}")
    public ResponseEntity<TeacherDTO> getTeacher(
            @Parameter(description = "ID univoco dell'insegnante da recuperare", example = "1")
            @PathVariable Integer varId) {
        return ResponseEntity.ok(service.getTeacher(varId));
        // TeacherDTO teachDto = service.getTeacher(varId);
        // // serve effettivamente id>=1?? || varId >= 1)

        // if (varId != null) {
        // return ResponseEntity.ok(teachDto);
        // }
        // return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Crea un nuovo insegnante",
            description = "Crea un nuovo insegnante nel sistema con i dettagli forniti"
    )
    @ApiResponse(responseCode = "201", description = "Insegnante creato")
    @ApiResponse(responseCode = "400", description = "Richiesta non valida")
    @PostMapping("")
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherRegisterRequest teachDTO) {
        TeacherDTO createdTeach = service.saveTeacher(teachDTO);
        return new ResponseEntity<>(createdTeach, HttpStatus.CREATED);

        // if (savedTeachDto != null) {
        // // URI localUri = URI.create("api/students/" + savedTeachDto.getId());
        // // return ResponseEntity.creeeeated(localUri).body(savedTeachDto);
        // return ResponseEntity.ok(savedTeachDto);
        // // best practirce crersi l'uri per i nuovi studenti
        // }
        // return ResponseEntity.badRequest().build();
    }

    @Operation (
            summary = "Aggiorna un insegnante esistente",
            description = "Aggiorna i dettagli di un insegnante specifico utilizzando il suo ID univoco"
    )
    @ApiResponse(responseCode = "200", description = "Insegnante aggiornato con successo")
    @ApiResponse(responseCode = "404", description = "Insegnante non trovato")
    @ApiResponse(responseCode = "409", description = "Conflitto durante l'aggiornamento dell'insegnante")
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(
            @Parameter(description = "ID univoco del professore", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody TeacherDTO teach) {
        // TeacherDTO newTeach = service.updateTeacher(id, teach);
        // if (newTeach != null) {
        // return ResponseEntity.ok().body(newTeach);
        // }
        // return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service.updateTeacher(id, teach));

    }

    @Operation(
            summary = "Elimina un insegnante per ID",
            description = "Elimina un insegnante specifico utilizzando il suo ID univoco"
    )
    @ApiResponse(responseCode = "204", description = "Insegnante eliminato con successo")
    @ApiResponse(responseCode = "404", description = "Insegnante non trovato")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(
            @Parameter(description = "ID univoco del professore da eliminare", example = "1")
            @PathVariable Integer id) {
        // boolean deleted = service.deleteTeacher(id);
        // if (deleted) {
        // return ResponseEntity.ok().body(true);
        // }
        // return ResponseEntity.badRequest().build();
        service.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

}
