package it.exprivia.Scuola.unit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import it.exprivia.Scuola.exception.DuplicateResourceException;
import it.exprivia.Scuola.exception.ResourceNotFoundException;
import it.exprivia.Scuola.mapper.StudentMapper;
import it.exprivia.Scuola.models.dto.StudentDTO;
import it.exprivia.Scuola.models.dto.StudentRegisterRequest;
import it.exprivia.Scuola.models.entity.Student;
import it.exprivia.Scuola.repositories.StudentRepository;
import it.exprivia.Scuola.services.impl.StudentServiceImpl;

class StudentServiceTests {

    private StudentServiceImpl studentServiceImpl;

    @Mock
    private StudentRepository studentRepositoryMock;

    @Mock
    private StudentMapper studentMapperMock;

    @Mock
    private BCryptPasswordEncoder studentPasswordEncoder;

    @BeforeEach
    void setup() {
        this.studentRepositoryMock = mock(StudentRepository.class);
        this.studentMapperMock = mock(StudentMapper.class);
        this.studentPasswordEncoder = mock(BCryptPasswordEncoder.class);
        this.studentServiceImpl = new StudentServiceImpl(studentMapperMock, studentRepositoryMock, studentPasswordEncoder);
    }

    // raggruppamento dei test per metodi
    @Nested
    class getStudentsMethods {

        @Test
        void should_ReturnCorrectListStudents() {
            // given

            List<Student> studentsEnt = new ArrayList<>();
            studentsEnt.add(new Student(null, null, null, null, null, null, null, null));
            studentsEnt.add(new Student(null, null, null, null, null, null, null, null));

            List<StudentDTO> studentDtos = new ArrayList<>();
            studentDtos.add(new StudentDTO(1, null, null, null, null, null, null));
            studentDtos.add(new StudentDTO(2, null, null, null, null, null, null));

            when(studentRepositoryMock.findAll()).thenReturn(studentsEnt);
            when(studentMapperMock.toDTOList(studentsEnt)).thenReturn(studentDtos);

            // when
            List<StudentDTO> actual = studentServiceImpl.getStudents();

            // then
            assertEquals(studentDtos, actual);

        }

        @Test
        void should_ReturnEmptyList_WhenNoStudentExist() {
            // given
            // when
            when(studentRepositoryMock.findAll()).thenReturn(new ArrayList<>());
            // utilizziamo l'anylist perchè va bene qualsiasi lista
            when(studentMapperMock.toDTOList(anyList())).thenReturn(new ArrayList<>());
            List<StudentDTO> actuals = studentServiceImpl.getStudents();

            // then
            assertNotNull(actuals);
            assertEquals(0, actuals.size());

        }
    }

    @Nested
    class getStudentByIdMethod {

        // @Test
        // void should_ReturnCorrectStudent_WithId() {

        // // given
        // Student stud = new Student("Simone", "Tondo", "001");
        // StudentDTO expected = new StudentDTO("Simone", "Tondo", "001");

        // // when
        // when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(stud));
        // when(studentMapperMock.toDTO(stud)).thenReturn(expected);
        // StudentDTO actual = studentServiceImpl.getStudent(1);

        // // then
        // assertEquals(expected, actual);

        // }

        @Test
        void should_ThrowException_WhenStudentNotFound() {
            // given

            // when
            when(studentRepositoryMock.findById(1)).thenReturn(Optional.empty());
            // then

            assertThrows(ResourceNotFoundException.class, () -> {
                studentServiceImpl.getStudent(1);
            });

        }

        @Test
        void should_ReturnCorrectDTO_WhenStudentExist() {
            // given
            Student stud = new Student(null, null, null, null, "Simone", "Tondo", "T001", null);
            StudentDTO expectedDTO = new StudentDTO(1, null, null, "Simone", "Tondo", "T001", null);

            // configurazione mock
            when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(stud));
            when(studentMapperMock.toDTO(stud)).thenReturn(expectedDTO);

            // when ( eseguiamo l'azione che vogliamo testare)
            StudentDTO actual = studentServiceImpl.getStudent(1);

            // then (verifichiamo il risultato)
            assertNotNull(actual);
            assertEquals(actual, expectedDTO);

        }
    }

    @Nested
    class deleteStudent {

        @Test
        void should_ThrowException_WhenDatabaseError() {
            //given
            Student student = new Student(null, null, "Simone", null, null, null, null, null);
            when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(student));
            //when
            // facciamo esplodere il db per simulare un errore e vediamo come si comporta il service
            // e capire se effettivamente l'eccezione viene propagata
            doThrow(new RuntimeException("Database error")).when(studentRepositoryMock).delete(student);
            //then
            assertThrows(RuntimeException.class, () -> studentServiceImpl.deleteStudent(1));

        }

        @Test
        void should_DeleteStudent_WhenStudentExist() {
            // given
            Student stud = new Student();
            stud.setUsername("enrico");
            when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(stud));

            // when
            studentServiceImpl.deleteStudent(1);

            // then
            verify(studentRepositoryMock).delete(stud);

        }

        @Test
        void should_ThrowException_WhenStudentNotFound() {
            // given
            int id = 100;
            // when
            when(studentRepositoryMock.findById(100)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> studentServiceImpl.deleteStudent(id));

            // then
            // in questo caso verifichiamo che, non esistendo lo studente con quell'id, non
            // venga mai chiamato il delete
            verify(studentRepositoryMock, never()).delete(any());
        }
    }

    @Nested
    class saveStudents {

        @Test
        void should_NotEncodeAndSave_WhenStuNumberDuplicated() {
            // given
            StudentRegisterRequest input = new StudentRegisterRequest(null, "xxx", "pwd", "simone", "tondo", "t001,", LocalDate.of(2025, 10, 10));
            Student existingStudent = new Student(null, null, "xxx", "xxx", "t001", null, null, null);

            when(studentRepositoryMock.findByStuNum(input.stuNum())).thenReturn(Optional.of(existingStudent));

            //when

            //then

            verify(studentPasswordEncoder, never()).encode(anyString());
            verify(studentRepositoryMock, never()).save(any());

        }

        @Test
        void should_SaveStudent_AndEncodePassword() {
            // given
            StudentRegisterRequest input = new StudentRegisterRequest(null, "Simone", "xxx1", "Simone", "Tondo", "T001", LocalDate.of(2025, 10, 12));
            Student savedStudent = new Student(null, null, "Simone", "xxx1", "Simone", "Tondo", "T001", LocalDate.of(2025, 10, 12));
            StudentDTO expectedDTO = new StudentDTO(1, null, "Simone", "Simone", "Tondo", "T001", LocalDate.of(2025, 10, 12));

            when(studentRepositoryMock.findByStuNum(anyString())).thenReturn(Optional.empty());
            when(studentPasswordEncoder.encode(anyString())).thenReturn("encodedPwd");
            when(studentMapperMock.toEntity(input)).thenReturn(savedStudent);
            when(studentRepositoryMock.save(savedStudent)).thenReturn(savedStudent);
            when(studentMapperMock.toDTO(savedStudent)).thenReturn(expectedDTO);

            // when
            StudentDTO result = studentServiceImpl.saveStudent(input);

            assertEquals(result, expectedDTO);
            // then
            verify(studentPasswordEncoder).encode("xxx1");
            verify(studentRepositoryMock).save(savedStudent);

            // ci assicuriamo che la password sia stafa effettivamente codificata quando salviamo l'entità
            assertThat(savedStudent.getPassword()).isEqualTo("encodedPwd");

        }

        @Test
        void should_SaveStudent_WhenValidDto() {
            // given
            StudentRegisterRequest input = new StudentRegisterRequest(null, "Flavio", null, null, null, "T001", null);
            Student savedStudent = new Student(null, null, "Flavio", null, null, null, "T001", null);
            StudentDTO expectedDTO = new StudentDTO(1, null, "Flavio", null, null, "T001", null);

            // when
            when(studentRepositoryMock.findByStuNum(input.stuNum())).thenReturn(Optional.empty()); // controllo
            // duplicati,
            // restituiamo un
            // optional vuoto
            when(studentMapperMock.toEntity(input)).thenReturn(savedStudent); // mappare l'input in entità
            when(studentRepositoryMock.save(savedStudent)).thenReturn(savedStudent); // salvare entità
            when(studentMapperMock.toDTO(savedStudent)).thenReturn(expectedDTO);// mappare entità salvata e restituire
            // un dto all'utente

            StudentDTO result = studentServiceImpl.saveStudent(input); // verifica della funzione di salvataggio

            // then
            verify(studentRepositoryMock).save(savedStudent); // verifica che viente salvato effettivamente lo stud
            assertEquals(1, result.id());
            assertNotNull(result);

        }

        @Test
        void should_ThrowException_WhenEmailDuplicated() {
            // given
            StudentRegisterRequest input = new StudentRegisterRequest("tr.simone@gmail.com", "Flavio", null, null, null, "T001", null);
            Student existingStudent = new Student(null, "tr.simone@gmail.com", "Lorenzo", null, null, null, "T001", null);

            // when

            when(studentRepositoryMock.findByEmail(input.email())).thenReturn(Optional.of(existingStudent));

            // then
            assertThrows(DuplicateResourceException.class, () -> studentServiceImpl.saveStudent(input));

            verify(studentRepositoryMock, never()).save(any());

        }

        @Test
        void should_ThrowException_WhenUsernameDuplicated() {
            //given
            StudentRegisterRequest input = new StudentRegisterRequest(null, "simone", null, null, null, null, null);
            Student stud = new Student();
            stud.setUsername("simone");

            //when
            when(studentRepositoryMock.findByUsername(input.username())).thenReturn(Optional.of(stud));
            //then
            assertThrows(DuplicateResourceException.class, () -> studentServiceImpl.saveStudent(input));
            verify(studentRepositoryMock, never()).save(any());
        }
    }

    @Nested
    class updateStudent {

        @Test
        void should_UpdateStudent_WhenStudentExist() {
            // given
            StudentDTO input = new StudentDTO(1, "tr.simone@gmail.com", "Simone", "TondoModificato", null, "T001", null); // contiene i dati che
            // vogliamo modificare

            Student existingStudent = new Student(null, null, "Enrico", null, null, null, null, null); // oggetto salvato
            // attualmente
            // nel db esistente

            when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(existingStudent));
            when(studentRepositoryMock.findByEmail("tr.simone@gmail.com")).thenReturn(Optional.empty());
            when(studentRepositoryMock.findByStuNum("T001")).thenReturn(Optional.empty());
            // quando viene chiamato
            // l'id
            // 1 risponde il db con
            // Existing
            when(studentRepositoryMock.save(any(Student.class))).thenReturn(existingStudent); // qualsiasi cosa ti venga
            // chiesto di salvare, salvalo, e restituisci l'oggetto
            when(studentMapperMock.toDTO(existingStudent)).thenReturn(input); // quando ricevi entità, torna il dto

            // when
            StudentDTO result = studentServiceImpl.updateStudent(input.id(), input);

            // then
            assertEquals(1, result.id());
            assertEquals(input, result);

            verify(studentRepositoryMock, times(1)).findByStuNum("T001");
            verify(studentRepositoryMock, times(1)).findByEmail("tr.simone@gmail.com");
            verify(studentRepositoryMock).save(existingStudent);

        }


        @Test
        void should_ThrowDuplicateResourceException_WhenChangingEmailButIsUsed() {
            // given
            Integer id = 1;
            StudentDTO input = new StudentDTO(id, "trdario@gmail.com", "user", "name", "lastname", "T001", LocalDate.of(2025, 10, 12)
            );
            Student existing = new Student(null, "tr.simone@gmail.com", null, null, null, null, null, LocalDate.of(2025, 10, 12));
            existing.setId(1);
            Student duplicated = new Student(null, "trdario@gmail.com", null, null, null, null, null, LocalDate.of(2025, 10, 12));
            duplicated.setId(2);

            when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(existing));
            when(studentRepositoryMock.findByEmail("trdario@gmail.com")).thenReturn(Optional.of(duplicated));

            // when
            // sbagliato ad utilizzare il service perchè vogliamo testare solamente l'eccezione
            // StudentDTO result = studentServiceImpl.updateStudent(input.id(), input);

            // then
            assertThrows(DuplicateResourceException.class, () -> studentServiceImpl.updateStudent(input.id(), input));
            // ci assicuriamo che una volta salvato per x motivo non venga mai chiamato il save
            verify(studentRepositoryMock, never()).save(any());
        }


        @Test
        void should_ThrowDuplicateResourceException_WhenChangingStuNumButIsUsed() {
            //given
            Integer id = 1;
            StudentDTO input = new StudentDTO(id, null, null, null, null, "T001", null);
            Student existing = new Student();
            existing.setId(1);
            Student duplicated = new Student();
            duplicated.setId(2);
            duplicated.setStuNum("T001");


            //when
            when(studentRepositoryMock.findById(1)).thenReturn(Optional.of(existing));
            when(studentRepositoryMock.findByStuNum("T001")).thenReturn(Optional.of(duplicated));

            //then
            assertThrows(DuplicateResourceException.class, () -> studentServiceImpl.updateStudent(input.id(), input));
            verify(studentRepositoryMock, never()).save(any());
        }

        @Test
        void should_NotThrowException_WhenEmailSameStudent() {
            //given
            Integer id = 1;
            StudentDTO input = new StudentDTO(id, "tr.simone@gmail.com", null, null, null, "T001", null);
            Student existing = new Student();
            existing.setId(1);

            //when
            when(studentRepositoryMock.findById(input.id())).thenReturn(Optional.of(existing));
            when(studentRepositoryMock.findByStuNum(input.stuNum())).thenReturn(Optional.of(existing));
            when(studentRepositoryMock.findByEmail(input.email())).thenReturn(Optional.of(existing));

            //then
            // verifichiamo che effettivamente entra dentro il filter e non scatena l'eccezione
            assertDoesNotThrow(() -> studentServiceImpl.updateStudent(id, input));
        }

        @Test
        void should_ThrowException_WhenIdNotExist() {
            // given
            Integer notExisting = 100;
            StudentDTO casual = new StudentDTO(notExisting, null, "user", "name", "lastname", "T001",
                    LocalDate.of(2025, 10, 12));

            when(studentRepositoryMock.findById(notExisting)).thenReturn(Optional.empty());
            // when
            assertThrows(ResourceNotFoundException.class, () -> studentServiceImpl.updateStudent(notExisting, casual));

            // then
            verify(studentRepositoryMock, never()).save(any());

        }
    }


    // non funziona il test perchè giustamente non utilizzo il mapper ma utilizzo
    // quello manuale nella classe student
    // @Test
    // void should_SaveStudent_WhenValidDto() {
    // // given
    // StudentDTO dto = new StudentDTO("Simone", "Tondo", "001");

    // Student entity = new Student(null, null, "Simone", "Tondo", "001", null);
    // Student savedEntity = new Student(null, null, "Simone", "Tondo", "001",
    // null);

    // when(studentMapperMock.toEntity(dto)).thenReturn(entity);
    // when(studentRepositoryMock.save(entity)).thenReturn(savedEntity);

    // // when
    // StudentDTO result = studentServiceImpl.saveStudent(dto);

    // // then
    // assertEquals(savedEntity, result);

    // verify(studentMapperMock).toEntity(dto);
    // verify(studentRepositoryMock).save(entity);
    // }

}
