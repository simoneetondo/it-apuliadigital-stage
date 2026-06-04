package it.exprivia.Scuola.services.impl;

import java.util.List;

import it.exprivia.Scuola.exception.DuplicateResourceException;
import it.exprivia.Scuola.models.dto.TeacherRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.exprivia.Scuola.exception.ResourceNotFoundException;
import it.exprivia.Scuola.mapper.TeacherMapper;
import it.exprivia.Scuola.models.dto.TeacherDTO;
import it.exprivia.Scuola.models.entity.Teacher;
import it.exprivia.Scuola.repositories.TeacherRepository;
import it.exprivia.Scuola.services.ITeacher;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements ITeacher {

    private final TeacherRepository repo;

    // inject del mapper
//    @Autowired
    private final TeacherMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;


//    public TeacherServiceImpl(TeacherRepository repo, TeacherMapper mapper) {
//        this.repo = repo;
//        this.mapper = mapper;
//    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return mapper.toDTOList(repo.findAll());
//        return repo.findAll()
//                .stream().map(x -> mapper.toDTO(x))
//                .toList();

        // puoi mappare direttamente la lista dal mapstruct e convertire tutte le entità
        // che hai nel repository in una lista dtos

        // List<TeacherDTO> dtos = mapper.toDTOList(repo.findAll());
        // return dtos;
    }

    @Override
    public TeacherDTO getTeacher(Integer id) {
        // non serve il controllo se abbiamo già la funzione che nel caso in cui non
        // trova l'id restituisce un null di per se
        // if (id != 0 && id >= 1) {} return null

        // calcolando che il findbyid restituisce un optional ( se non trova nulla
        // potrebbe essere vuoto )
        // mentre il mapper vuole un teacher e quindi dobbiamo estrarlo dall'optional

        return repo.findById(id)
                .map(teach -> mapper.toDTO(teach))
                // .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Utente con id: " + id + " non esistente."));
    }

    @Override
    public void deleteTeacher(Integer id) {
        Teacher teach = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente con id:" + id + " non esistente."));
        repo.delete(teach);
        // if (id == 0 || id == null) {
        // return false;
        // }
        // if (!repo.existsById(id)) {
        // return false;
        // }
        // repo.deleteById(id);
        // return true;
    }

    // questa sarebbe la register, non ha senso il save puro
    @Override
    public TeacherDTO saveTeacher(TeacherRegisterRequest teachDTO) {
        // se l'utente creato è diverso da null
        if (repo.findByEmail(teachDTO.email()).isPresent()) {
            throw new DuplicateResourceException("Utente con email: " + teachDTO.email() + " già presente.");
        }
        // creiamo l'entità e la mappiamo all'oggetto creato e la salviamo
        Teacher teacher = mapper.toEntity(teachDTO);

        String cryptedPassword = passwordEncoder.encode(teachDTO.password());
        teacher.setPassword(cryptedPassword);

        Teacher savedTeacher = repo.save(teacher);
        // ci facciamo restituire sempre un dto
        // cosi' come in student dato che nel dto comunque non richiediamo l'id e non lo
        // autogenera dobbiamo prendercelo dall'entity
        // teachDTO.setId(teacher.getId());
        return mapper.toDTO(savedTeacher);

    }

    @Override
    public TeacherDTO updateTeacher(Integer id, TeacherDTO newTeach) {

        Teacher teach = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente con id: " + id + "non trovato"));

        repo.findByEmail(newTeach.email())
                .filter(t -> !t.getId().equals(id))
                        .ifPresent(t -> {
                            throw new DuplicateResourceException("Email " + newTeach.email() + " già esistente");
                        });

        // metodo con il mapper
        mapper.updateTeacherFromDTO(newTeach, teach);
//        teach.setFirstName(newTeach.firstName());
//        teach.setLastName(newTeach.lastName());
//        teach.setTeacherSub(newTeach.teacherSub());
        Teacher updatedTeach = repo.save(teach);
        // altrimenti dato che non richiediamo l'id ci restituisce comunque un dto con
        // l'id null perchè non lo autogenera
        // newTeach.setId(updatedTeacher.getId());
        return mapper.toDTO(updatedTeach);

    }

}
