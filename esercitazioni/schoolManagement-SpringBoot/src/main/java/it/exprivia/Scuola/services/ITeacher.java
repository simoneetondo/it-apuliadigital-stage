package it.exprivia.Scuola.services;

import java.util.List;

import it.exprivia.Scuola.models.dto.TeacherDTO;
import it.exprivia.Scuola.models.dto.TeacherRegisterRequest;

public interface ITeacher {

    public List<TeacherDTO> getAllTeachers();

    public TeacherDTO getTeacher(Integer id);

    public void deleteTeacher(Integer id);

    public TeacherDTO saveTeacher(TeacherRegisterRequest teachDTO);

    // qualora dovessimo modificare la materia dell'insegnante
    
    public TeacherDTO updateTeacher(Integer id, TeacherDTO teach);
    
    // login da dividere sia nel controller sia nel service creando una login service

}
