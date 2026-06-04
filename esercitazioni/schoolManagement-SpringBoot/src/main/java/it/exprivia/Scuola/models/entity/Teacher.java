package it.exprivia.Scuola.models.entity;

import it.exprivia.Scuola.models.abstracts.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Teacher extends Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String teacherSub;

	// costruttore vuoto

	public Teacher() {
		super();
	}

	// costruttore con parametri che chiedi al momento della registrazione 

	public Teacher(String role, String email, String username, String password, String firstName, String lastName, String teacherSub) {
		super(role, email, username, password,firstName, lastName);
		this.teacherSub = teacherSub;
	}	

	// getters and setters

	public Integer getId() {
		return this.id;
	}

	public String getTeacherSub() {
		return teacherSub;
	}

	public void setTeacherSub(String teacherSub) {
		this.teacherSub = teacherSub;
	}

	// to string che include gli attributi della superclase e fa sempre comodo

	// se utilizziamo protected possiamo inserire direttamente il campo per ereditarlo 
	// ma la best practice è quella di utilizzare i getter e i setter
	@Override
	public String toString() {
		return "Teacher [teacherSub=" + teacherSub + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + "]";
	}

}
