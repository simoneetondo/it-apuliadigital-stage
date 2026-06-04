package it.exprivia.Scuola.mapper;

import java.util.List;

import org.mapstruct.*;

import it.exprivia.Scuola.models.dto.StudentDTO;
import it.exprivia.Scuola.models.dto.StudentRegisterRequest;
import it.exprivia.Scuola.models.entity.Student;

// al momento non viene utilizzato per lo student
// mapstruct genererà un bean di spring per il mapper e puoi utilizzarlo per gestire i componenti

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true) // ignora il campo dato che non lo abbiamo inserito nel register
    @Mapping(target = "password", ignore = true) // Impedisce la copia della password in chiaro
    Student toEntity(StudentRegisterRequest studentReg);

    StudentDTO toDTO(Student student);

    List<StudentDTO> toDTOList(List<Student> students);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Student updateStudentFromDTO(StudentDTO studDTO, @MappingTarget Student stud);
}

