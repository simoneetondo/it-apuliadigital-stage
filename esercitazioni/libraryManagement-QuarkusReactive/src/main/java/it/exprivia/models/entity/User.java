    package it.exprivia.models.entity;

    import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;

    import java.time.LocalDate;
    import java.util.Date;
    import java.util.Objects;

    @Entity
    @Table(name = "users")
    public class User extends PanacheEntityBase {

        @Id
        @Column(name = "codice_fiscale", nullable = false, unique = true)
        private String codiceFiscale;
        @Column(name = "first_name", nullable = false)
        private String firstName;
        @Column(name = "last_name", nullable = false)
        private String lastName;
        @Column(name = "email", nullable = false, unique = true)
        private String email;
        @Column(name = "phone_number", nullable = false, unique = true)
        private String phoneNumber;
        @Column(name = "date_of_birthday", nullable = false)
        private Date dateOfBirth;


    // empty constructor
    public User() {
    }

    // constructor with fields
    public User(String codiceFiscale, String firstName, String lastName, String email, String phoneNumber, Date dateOfBirth) {
        this.codiceFiscale = codiceFiscale;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // getter and setters

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // to string

    @Override
    public String toString() {
        return "User{" +
                "codiceFiscale='" + codiceFiscale + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate=" + dateOfBirth +
                '}';
    }

    // equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(codiceFiscale, user.codiceFiscale) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceFiscale, firstName, lastName, email, phoneNumber, dateOfBirth);
    }
}
