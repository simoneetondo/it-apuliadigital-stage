package it.exprivia.Scuola.services.impl;

import it.exprivia.Scuola.exception.UnauthorizedException;
import it.exprivia.Scuola.mapper.StudentMapper;
import it.exprivia.Scuola.mapper.TeacherMapper;
import it.exprivia.Scuola.models.dto.LoginRequest;
import it.exprivia.Scuola.models.dto.LoginResponse;
import it.exprivia.Scuola.models.entity.Student;
import it.exprivia.Scuola.models.entity.Teacher;
import it.exprivia.Scuola.repositories.StudentRepository;
import it.exprivia.Scuola.repositories.TeacherRepository;
import it.exprivia.Scuola.security.JwtService;
import it.exprivia.Scuola.services.ILogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // genera i costruttori con tutti i campi final
public class AuthServiceImpl implements ILogin {

    // 1. quando mettiamo final ci assicuriamo che non può essere piu cambiato

    // 2. niente NullPointer, con autowired spring prova a iniettare la dipendenza dopo aver creato l'oggetto
    // se la dipendenza manca l'oggetto viene creato con quel campo a null ed esploderà soltanto all'utilizzo

    // 3. UnitTesting, se usiamo Autowired per fare i test devi avviare per forza tutto il contesto spring (lento)
    // se usiamo il costruttore possiamo terstare la classe come le classiche classi java, new loginserviceimpl(mockedRepo, mockedMapper, ecc)
    // è istantaneo e non serve spring per testare logica

    // 4. pulizia codice

    private final BCryptPasswordEncoder passwordEncoder;
    private final StudentRepository studentRepo;
    private final JwtService jwtService;
    private final TeacherRepository teacherRepo;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;


    // Costruttore per la Dependency Injection
//    public AuthServiceImpl(BCryptPasswordEncoder passwordEncoder,
//                            StudentRepository studentRepo,
//                            TeacherRepository teacherRepo,
//                            StudentMapper studentMapper,
//                            TeacherMapper teacherMapper) {
//        this.passwordEncoder = passwordEncoder;
//        this.studentRepo = studentRepo;
//        this.teacherRepo = teacherRepo;
//        this.teacherMapper = teacherMapper;
//        this.studentMapper = studentMapper;
//    }


    // utilizziamo la logj request anzichè dei semplici campi username e password perchè:
    // 1. in futuro possiamo aggiungere quali campi vogliamo
    // 2. possiamo utilizzare lo stesso dto in altri contesti
    // 3. codice leggibile
    // 4. possiamo aggiungere campi di validazione nel DTO (notblank, notnull, etc

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // cerchiamo lo studente
        Optional<Student> studOpt = studentRepo.findByUsername(loginRequest.username());
        if (studOpt.isPresent()) {
            Student stud = studOpt.get();
            if (!passwordEncoder.matches(loginRequest.password(), stud.getPassword())) {
                throw new UnauthorizedException("Username o password errati.");
            }
            // AGGIUNTA TOKEN JWT 13/01
            String token = jwtService.generateToken(stud.getUsername(), "STUDENT");
            return new LoginResponse("Login effettuato con successo", "STUDENT", token ,studentMapper.toDTO(stud));
        }

        Optional<Teacher> teachOpt = teacherRepo.findByUsername(loginRequest.username());
        if (teachOpt.isPresent()) {
            Teacher teach = teachOpt.get();
            if (!passwordEncoder.matches(loginRequest.password(), teach.getPassword())) {
                throw new UnauthorizedException("Username o password errati.");
            }
            // AGGIUNTA TOKEN JWT 13/01
            String token = jwtService.generateToken(teach.getUsername(), "TEACHER");
            return new LoginResponse("Login effettuato con successo", "TEACHER", token, teacherMapper.toDTO(teach));
        }

        throw new UnauthorizedException("Username o password errati.");
        // aggiungere logica con token JWT per sessioni
        // aggiungere ruoli
        // spostare il saveStudent/teacher in un service a parte e chiamarlo register
    }
}



//        Student stud = studentRepo.findByUsername(username).
//                orElseThrow(() -> new UnauthorizedException("Username o password errati."));
//        Teacher teach = teacherRepo.findByUsername(username).
//                orElseThrow(() -> new UnauthorizedException("Username o password errati."));
//
//        if (!passwordEncoder.matches(password, stud.getPassword()) || !passwordEncoder.matches(password, teach.getPassword())) {
//            throw new UnauthorizedException("Username o password errati.");
//        }

//        String storedPassword = studentRepo.findByUsername(username)
//                .map(stud -> stud.getPassword())
//                .orElseGet(() -> teacherRepo.findByUsername(username)
//                        .map(teach -> teach.getPassword())
//                        .orElseThrow(() -> new UnauthorizedException("Username o password errati.")));
//
//        if (!passwordEncoder.matches(password, storedPassword)) {
//            throw new UnauthorizedException("Username o password errati.");
//        }
//        return new LoginResponse("Login effettuato con successo")


