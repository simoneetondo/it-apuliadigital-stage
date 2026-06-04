package it.exprivia.Scuola.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.exprivia.Scuola.models.entity.Student;

@Repository
public interface StudentRepository extends IPersonRepository<Student> {
	
    // ricerca del numero dello studente per evitare la creazione di uno studente con un numero uguale a quello di un'altro
	Optional<Student> findByStuNum(String stuNum);

    List<Student> findByFirstNameContainingIgnoreCase(String firstName);




}
