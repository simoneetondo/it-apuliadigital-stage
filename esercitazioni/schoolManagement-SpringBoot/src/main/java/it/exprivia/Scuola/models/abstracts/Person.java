package it.exprivia.Scuola.models.abstracts;

import jakarta.validation.constraints.Email;
import org.hibernate.mapping.Column;

import jakarta.persistence.MappedSuperclass;

// Questo dice a JPA di non creare una tabella Person, ma di aggiungere i campi username, password, ecc., nelle tabelle dei figli 
@MappedSuperclass
public abstract class Person {
    // mettendo protected i figli che ereditano vedono gli attributi e non sono
    // visibili solo a questa classe
    protected String role;
    protected String email;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;

    // consturctors

    public Person() {

    }

    public Person(String email, String username, String password, String firstName, String lastName, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role= role;
    }

    // getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // to string

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}
