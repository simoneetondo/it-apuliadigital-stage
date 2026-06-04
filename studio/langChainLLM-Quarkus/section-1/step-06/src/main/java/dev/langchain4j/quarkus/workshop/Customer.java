package dev.langchain4j.quarkus.workshop;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.Optional;

@Entity
public class Customer extends PanacheEntity {

    String firstName;
    String lastName;

    public static Optional<Customer> findByFirstAndLastName(String firstName, String lastName) {
        return find("firstName = ?1 and lastName = ?", firstName, lastName).firstResultOptional();
    }


}
