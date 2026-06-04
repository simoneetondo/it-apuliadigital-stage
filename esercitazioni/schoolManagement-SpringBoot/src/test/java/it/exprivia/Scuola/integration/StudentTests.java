package it.exprivia.Scuola.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.exprivia.Scuola.models.dto.StudentDTO;
import it.exprivia.Scuola.models.dto.StudentRegisterRequest;
import it.exprivia.Scuola.models.entity.Student;
import it.exprivia.Scuola.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// carica l'intera applicazione spring (bean, service, repository, db, etc)
// random port si utilizza per dire a spring di non avviare il server sulla porta 8080 di default ma di utilizzarne
// unaa libera a caso
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// abilita l'iniezione di MockMvc per fare chiamate HTTP simulate
@AutoConfigureMockMvc
public class StudentTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        repo.deleteAll();
    }

    @Nested
    class GetTests {

        @Test
        void should_SaveAndRetrieveStudent_When_ValidData() throws Exception {
            // GIVEN
            Student student = new Student();
            student.setFirstName("Mario");
            student.setLastName("Rossi");
            // Non settiamo l'ID manualmente, lasciamo fare al DB
            Student savedStudent = repo.save(student);

            // WHEN & THEN
            mockMvc.perform(get("/api/students/{id}", savedStudent.getId()))
                    .andDo(print()) // Ottimo per vedere il JSON reale in console se fallisce
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.lastName").value("Rossi"))
                    .andExpect(jsonPath("$.firstName").value("Mario"));
        }

        @Test
        void should_Return404_When_InvalidId() throws Exception {
            // given
            long idNotExist = 999;
            // when
            mockMvc.perform(get("/api/students/{id}", idNotExist))
                    // then
                    .andDo(print()) // Ottimo per vedere il JSON reale in console se fallisce
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Utente con id: " + idNotExist + " non presente."));
        }

        @Test
        void should_ReturnAllStudents_When_MultipleStudentsExist() throws Exception {
            //given
            repo.save(new Student());
            repo.save(new Student());

            //when
            mockMvc.perform(get("/api/students"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        void should_ReturnEmptyList_When_NoStudentsExist() throws Exception {
            //when
            mockMvc.perform(get("/api/students"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    class PostTests {
        @Test
        void should_CreateStudent_When_ValidData() throws Exception {
            // given
            //giustamente prima cadeva in errore perchè inserivo un
            StudentRegisterRequest input = new StudentRegisterRequest(
                    "tr.simone@gmail.com", "simonee", "Password123!", "Simone", "Tondo", "ST001", LocalDate.of(2000, 6, 10));

            mockMvc.perform(post("/api/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    //then
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value("Simone"))
                    .andExpect(jsonPath("$.id").exists());


        }

        @Test
        void should_ReturnValidationException_When_InvaldData() throws Exception {

            StudentRegisterRequest input = new StudentRegisterRequest("tr.simone@gmail.com", "simonee", "password", "Simone", "Tondo", "ST001", LocalDate.of(2000, 6, 10));

            mockMvc.perform(post("/api/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    //then
                    // VALIDATION EXCEPTION, 400 BAD REQUEST
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_ReturnDuplicateException_When_EmailOrStuNumAlreadyExist() throws Exception {
            //given
            Student existing = new Student();
            existing.setEmail("tr.simone@gmail.com");
            existing.setStuNum("ST001");
            repo.save(existing);

            StudentRegisterRequest input = new StudentRegisterRequest("tr.simone@gmail.com", "simonee", "Password123!", "Simone", "Tondo", "ST002", LocalDate.of(2000, 6, 10));

            //when
            mockMvc.perform(post("/api/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    //then
                    .andDo(print())
                    .andExpect(status().isConflict())
            ;
        }

    }

    @Nested
    class DeleteTests {
        @Test
        void should_ReturnNoContent_When_DeleteExistingStudent() throws Exception {
            //given
            Student existing = new Student();
            existing.setId(1);
            repo.save(existing);
            //when
            mockMvc.perform(delete("/api/students/{id}", existing.getId()))
                    //then
                    .andDo(print())
                    .andExpect(status().isNoContent());

        }

        @Test
        void should_ReturnNotFound_When_DeleteNonExistingStudente() throws Exception {
            //given
            long idNotExist = 999;
            //when
            mockMvc.perform(delete("/api/students/{id}", idNotExist))
                    //then
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }


    @Nested
    class PutTests {
        @Test
        void should_UpdateStudent_When_ValidData() throws Exception {
            //given
            Student existing = new Student();
            existing.setFirstName("Mario");
            existing.setLastName("Rossi");
            Student savedStudent = repo.save(existing);
            Integer id = savedStudent.getId();

            StudentDTO input = new StudentDTO(id, "tr.simone@gmail.com", "simonee", "Simone", "Tondo", "ST001", LocalDate.of(2000, 6, 10));

            //when
            mockMvc.perform(put("/api/students/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Simone"))
                    .andExpect(jsonPath("$.lastName").value("Tondo"));
        }


        @Test
        void should_ReturnNotFound_When_StudentToUpdateNotExist() throws Exception {
            // given
            Integer id = 999;
            StudentDTO input = new StudentDTO(id, "tr.simone@gmail.com", "simonee", "Simone", "Tondo", "ST001", LocalDate.of(2000, 6, 10));

            // when
            mockMvc.perform(put("/api/students/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    // then
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void should_ReturnDuplicateException_When_StuNumOrEmailUpdatedAlreadyExist() throws Exception {
            // given
            Student existing = new Student();
            existing.setStuNum("T001");
            existing.setEmail("tr.simone@gmail.com");
            Student savedStudent = repo.save(existing);
            Integer id = savedStudent.getId();

            Student anotherStudent = new Student();
            anotherStudent.setId(3);
            repo.save(anotherStudent);
            Integer anotherId = anotherStudent.getId();

            StudentDTO input = new StudentDTO(anotherId, "tr.simone@gmail.com", "simonee", "Simone", "Tondo", "ST005", LocalDate.of(2000, 6, 10));
            // when
            mockMvc.perform(put("/api/students/{id}", anotherId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    // then
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }


}