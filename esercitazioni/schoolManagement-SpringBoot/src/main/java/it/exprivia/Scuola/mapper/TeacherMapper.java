package it.exprivia.Scuola.mapper;

import java.util.List;

import it.exprivia.Scuola.models.dto.TeacherRegisterRequest;
import org.mapstruct.*;

import it.exprivia.Scuola.models.dto.TeacherDTO;
import it.exprivia.Scuola.models.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "id", ignore = true) // ignora il campo, dato che non lo abbiamo nemmeno inserito in register
    @Mapping(target = "password", ignore = true)
        // Impedisce la copia della password in chiaro
    Teacher toEntity(TeacherRegisterRequest teachReg);

    TeacherDTO toDTO(Teacher teacher);

    List<TeacherDTO> toDTOList(List<Teacher> teachers);

    List<Teacher> toEntityList(List<TeacherDTO> teachersDTO);

    // il nullValue serve per far ignorare al db dei campi potenzialmente nulli, e non farli sovrascrivere
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Teacher updateTeacherFromDTO(TeacherDTO teacherDTO, @MappingTarget Teacher teacher);
    // con il @MappingTarget: MapStruct capisce che deve prendere l'oggetto stud/teach esistente, quello che abbiamo
    // recuperato nel db dal servicem e aggiornarlo con i dati del DTO.
    // senza MappingTarget: MapStruct cerca di crare un nuovo oggetto e non di aggiornare quello esistente

}
