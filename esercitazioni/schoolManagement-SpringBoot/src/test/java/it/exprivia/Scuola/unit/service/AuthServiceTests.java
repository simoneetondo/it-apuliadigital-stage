// java
package it.exprivia.Scuola.unit.service;

import it.exprivia.Scuola.exception.UnauthorizedException;
import it.exprivia.Scuola.mapper.StudentMapper;
import it.exprivia.Scuola.mapper.TeacherMapper;
import it.exprivia.Scuola.models.dto.LoginRequest;
import it.exprivia.Scuola.models.entity.Student;
import it.exprivia.Scuola.models.entity.Teacher;
import it.exprivia.Scuola.repositories.StudentRepository;
import it.exprivia.Scuola.repositories.TeacherRepository;
import it.exprivia.Scuola.security.JwtService;
import it.exprivia.Scuola.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTests {

    private AuthServiceImpl authService;
    private BCryptPasswordEncoder passwordEncoder;
    private StudentRepository studentRepo;
    private TeacherRepository teacherRepo;
    private JwtService jwtService;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;

    @BeforeEach
    void setup() {
        this.passwordEncoder = mock(BCryptPasswordEncoder.class);
        this.studentRepo = mock(StudentRepository.class);
        this.teacherRepo = mock(TeacherRepository.class);
        this.jwtService = mock(JwtService.class);
        this.studentMapper = mock(StudentMapper.class);
        this.teacherMapper = mock(TeacherMapper.class);

        this.authService = new AuthServiceImpl(
                passwordEncoder,
                studentRepo,
                jwtService,
                teacherRepo,
                studentMapper,
                teacherMapper
        );
    }

    @Test
    void should_ThrowException_WhenUsernameNotFound() {
        // given
        LoginRequest loginRequest = new LoginRequest("simonetta", "loreta123!");
        // when
        when(studentRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(UnauthorizedException.class, () -> {
            authService.login(loginRequest);
        });
    }

    @Test
    void should_MatchPassword_WhenUsernameValid() {
        // given
        Student stud = new Student();
        stud.setUsername("simonetta");
        stud.setPassword("encodedPassword");
        LoginRequest loginRequest = new LoginRequest("simonetta", "loreta123!");
        // when
        when(studentRepo.findByUsername(anyString())).thenReturn(Optional.of(stud));
        when(passwordEncoder.matches("loreta123!", "encodedPassword")).thenReturn(true);
        authService.login(loginRequest);
        // then
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());

    }


    @Test
    void should_GenerateToken_WhenLoginSuccessful() {
        //given
        Student stud = new Student();
        stud.setUsername("simonetta");
        stud.setPassword("encodedPassword");
        LoginRequest loginRequest = new LoginRequest("simonetta", "loreta123");
        //when
        when(studentRepo.findByUsername(anyString())).thenReturn(Optional.of(stud));
        when(passwordEncoder.matches("loreta123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("mocked-jwt-token");
        authService.login(loginRequest);
        //then
        verify(jwtService, times(1)).generateToken(anyString(), anyString());
    }

    @Test
    void should_Unauthorized_When_PasswordNotMatchStudent() {
        Student stud = new Student();
        stud.setUsername("simone");
        stud.setPassword("encodedPassword");
        LoginRequest login = new LoginRequest("simone", "password123");

        when(studentRepo.findByUsername(anyString())).thenReturn(Optional.of(stud));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> authService.login(login));

    }

    @Test
    void should_Unauthorized_When_PasswordNotMatchTeacher() {
        Teacher teach = new Teacher();
        teach.setUsername("simone");
        teach.setPassword("encodedPassword");
        LoginRequest login = new LoginRequest("simone", "password123");

        when(teacherRepo.findByUsername(anyString())).thenReturn(Optional.of(teach));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> authService.login(login));

    }

    @Test
    void should_Authorized_When_PasswordMatchTeacher() {
        Teacher teach = new Teacher();
        teach.setUsername("simone");
        teach.setPassword("encodedPassword");
        LoginRequest login = new LoginRequest("simone", "password123");

        when(teacherRepo.findByUsername(anyString())).thenReturn(Optional.of(teach));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        authService.login(login);
    }


}
