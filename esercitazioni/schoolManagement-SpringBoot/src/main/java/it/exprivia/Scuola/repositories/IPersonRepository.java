package it.exprivia.Scuola.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

// INFERENZA DEL GENERICO TIPO
// T = TYPE
// IRepository ha bisogno del tipo, se metto studente nell'IRepository va a mettere l'oggetto student anche in teacher
// stiamo defindendo in maniera dinamica quello che è il tipo all'interno della repository

/**/

// PRINCIPIO DELLA SEGREGAZIONE

@NoRepositoryBean
public interface IPersonRepository<T> extends JpaRepository<T, Integer> {
    // creiamo il metodo su IPerson perchè lo implementano sia teacher sia student
    Optional<T> findByUsername(String username);
    Optional<T> findByEmail(String email);

    // concatenare altri metodi comuni

}
