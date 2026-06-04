package it.exprivia.Scuola.services;

import java.util.List;

import it.exprivia.Scuola.models.dto.StudentDTO;
import it.exprivia.Scuola.models.dto.StudentRegisterRequest;
import it.exprivia.Scuola.models.entity.Student;

public interface IStudent {
	
		public List<StudentDTO> getStudents();
	
		public StudentDTO getStudent(Integer id);	
		
		public StudentDTO saveStudent(StudentRegisterRequest studDTO);
		
		public void deleteStudent(Integer id);
		
		// update student al momento senza senso perchè è servirebbe qualora inseriamo indirizzi

		// in input si 
		public StudentDTO updateStudent(Integer id, StudentDTO newStudent);

		//
	public List<StudentDTO> getStudentsByName(String name);
}
