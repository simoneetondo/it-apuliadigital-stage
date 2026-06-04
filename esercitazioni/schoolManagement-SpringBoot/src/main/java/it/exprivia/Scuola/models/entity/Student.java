package it.exprivia.Scuola.models.entity;

import java.sql.Date;
import java.time.LocalDate;

import it.exprivia.Scuola.models.abstracts.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class Student extends Person {

	// generazione automatica dell'id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String stuNum;
	private LocalDate dateBr;

	// costruttore vuoto
	public Student() {
		super();
	}

	// costruttore con param
	// Non si utilizza piu Date perchè è deprecato, si utilizza LocalDate / LocalDateTime
	public Student(String role, String email, String username, String password, String firstName, String lastName, String stuNum, LocalDate dateBr) {
		super(role, email, username, password, firstName, lastName);
		this.stuNum = stuNum;
		this.dateBr = dateBr;
	}

	// costruttore che ci serve solo per i test
	public Student(String firstName, String lastName, String stuNum) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.stuNum = stuNum;
	}

	// getters and setters

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	public LocalDate getDateBr() {
		return dateBr;
	}

	public void setDateBr(LocalDate dateBr) {
		this.dateBr = dateBr;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id){
		this.id= id;
	}

	// to string che include gli attributi della superclase e fa sempre comodo

	@Override
	public String toString() {
		return "Student [stuNum=" + stuNum + ", dateBr=" + dateBr + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}

}
