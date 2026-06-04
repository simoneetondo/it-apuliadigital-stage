package it.exprivia.Scuola.services.impl;

import java.util.List;

import it.exprivia.Scuola.annotation.TrackExecution;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.exprivia.Scuola.exception.DuplicateResourceException;
import it.exprivia.Scuola.exception.ResourceNotFoundException;
import it.exprivia.Scuola.mapper.StudentMapper;
import it.exprivia.Scuola.models.dto.StudentDTO;
import it.exprivia.Scuola.models.dto.StudentRegisterRequest;
import it.exprivia.Scuola.models.entity.Student;
import it.exprivia.Scuola.repositories.StudentRepository;
import it.exprivia.Scuola.services.IStudent;

@Service
@RequiredArgsConstructor // vedere commenti login
public class StudentServiceImpl implements IStudent {

    //    @Autowired
    private final StudentMapper mapper;
    private final StudentRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;

    // utilizzare il costruttore per l'iniezione delle dependency
    // Problema: il test crea new StudentServiceImpl (studentMapperMock,
    // studentRepositoryMock)  ma la classe non ha quel costruttore, quindi il
    // compilatore segnala l'errore.

//    public StudentServiceImpl(StudentMapper mapper, StudentRepository repo, BCryptPasswordEncoder passwordEncoder) {
//        this.mapper = mapper;
//        this.repo = repo;
//        this.passwordEncoder = passwordEncoder;
//    }

    // bisogna utilizzare il mapper se lo si vuole utilizzare anche nei test


    @Override
    public List<StudentDTO> getStudents() {
        return mapper.toDTOList(repo.findAll());


        // List<StudentDTO> dtos = mapper.toDTOList(repo.findAll());
        // return dtos;
    }

    @Override
    public StudentDTO getStudent(Integer id) {
        // troviamo tramite il repo l'id e mappiamo lo studente trovato in un dto,
        // altrimenti è null
        // se lo studente viene trovato lo mappa direttamente in dto altrimenti scatena
        // eccezione

        return repo.findById(id).map(stud -> mapper.toDTO(stud))
                // .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Utente con id: " + id + " non presente."));

        // Student stud = repo.findById(id).orElse(null);
        // StudentDTO studDTO = new StudentDTO();
        // // studDTO.setId(stud.getId());
        // studDTO.setFirstName(stud.getFirstName());
        // studDTO.setLastName(stud.getLastName());
        // studDTO.setDateBr(stud.getDateBr());
        // studDTO.setStuNum(stud.getStuNum());
        // return studDTO;
    }

    // ci andiamo a richiamare lo stuNum come identificativo
    // se non esiste andiamo a crearci l'entità, ci settiamo i campi che riceviamo nella registrazione
    // la salviamo e facciamo il return direttamente del DTO tramite il mapper che
    // restituisce un oggetto di tipo studentDTO SENZA LA PASSWORD
    @Transactional
    @Override
    @TrackExecution
    public StudentDTO saveStudent(StudentRegisterRequest studReg) {

        // al momento unica verifica sensata tramite il numero che deve essere univoco
        // non chiedendo l'id
        repo.findByEmail(studReg.email()).ifPresent(s -> {
            throw new DuplicateResourceException("Email " + studReg.email() + " già esistente");
        });

        repo.findByUsername(studReg.username()).ifPresent(s -> {
            throw new DuplicateResourceException("Username " + studReg.username() + " già esistente");
        });

        // creiamo un nuovo mapper che mappa la richiesta di registrazione in entità
        Student student = mapper.toEntity(studReg);

        String cryptedPassword = passwordEncoder.encode(studReg.password());
        student.setPassword(cryptedPassword);

        // student.setUsername(studReg.username());
        // student.setPassword(studReg.password());
        // student.setFirstName(studReg.firstName());
        // student.setLastName(studReg.lastName());
        // student.setDateBr(studReg.dateBr());
        // student.setStuNum(studReg.stuNum());

        repo.save(student);
        return mapper.toDTO(student);
    }


    // SOFT_DELETE? per i casi di scuola è utile una volta eliminato uno studente
    // avere in memoria i voti o esami a lui annessi
    @Override
    public void deleteStudent(Integer id) {
        // verify o strategy? springboot
        // se non viene trovato con quell'id viene sollevata un'eccezione, se lo trova
        // procede e lo elimina dal repo
        Student stud = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Studente con id:" + id + " non trovato"));
        repo.delete(stud);
    }

    // @Transactional , Hibernate si accorge da solo se hai cambiato i dati
    // dell'oggetto student, forse potrebbe
    // anche non servire il save(student)

    // rimodernizzato, abbiamo tolto il return null perchè se non lo trova solleva
    // direttamente un'eccezione
    // se lo trova invece vuol dire che non è entrato nell'eccezione e possiamo
    // direttamente settarli i param che ci fornisce l'utente

    @Override
    public StudentDTO updateStudent(Integer id, StudentDTO newStudent) {
        Student student = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Studente con id:" + id + " non presente."));

        repo.findByEmail(newStudent.email())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> {
                    throw new DuplicateResourceException("Email " + newStudent.email() + " già in uso.");
                });

        repo.findByStuNum(newStudent.stuNum())
                .filter(s -> !s.getId().equals(id)) // cicliamo tra tutti gli studenti, e prendiamo lo studente solo
                                                            // se l'id  è diverso da quello inserito inizialmente
                .ifPresent(s -> {
                    throw new DuplicateResourceException("Identificativo " + newStudent.stuNum() + " già presente.");
                });

        mapper.updateStudentFromDTO(newStudent, student);
//        student.setUsername(newStudent.username());
//        student.setFirstName(newStudent.firstName());
//        student.setLastName(newStudent.lastName());
//        student.setStuNum(newStudent.stuNum());
//        student.setDateBr(newStudent.dateBr());

        Student updatedStud = repo.save(student);

        return mapper.toDTO(updatedStud);
        // studDTO.setId(updatedStudent.getId());
    }

    @Override
    public List<StudentDTO> getStudentsByName(String name) {
        return repo.findByFirstNameContainingIgnoreCase(name)
                .stream()
                .map(s -> mapper.toDTO(s))
                .toList();


    }

}
