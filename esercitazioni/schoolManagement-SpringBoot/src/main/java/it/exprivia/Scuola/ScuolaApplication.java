package it.exprivia.Scuola;

import it.exprivia.Scuola.models.entity.Student;
import it.exprivia.Scuola.repositories.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ScuolaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScuolaApplication.class, args);
	}

	// METODO CREATO DA CHAT PER FARMI CREARE UN UTENTE INIZIALMENTE SE NON HO DB PER FARE LA LOGIN

	@Bean
	public CommandLineRunner initDatabase(StudentRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			// controlliamo se l'utente esiste già per non crearne mille duplicati
			if (repository.findByUsername("admin").isEmpty()) {
				Student stud = new Student();
				stud.setUsername("admin");
				// Uiamo il passwordEncoder che abbiamo definito nella BeansConfig o SecurityConfig
				stud.setPassword(passwordEncoder.encode("password123"));
				stud.setRole("STUDENT");

				repository.save(stud);
				System.out.println("--------------------------------------------------");
				System.out.println("DATABASE INIZIALIZZATO: admin / password123");
				System.out.println("--------------------------------------------------");
			}
		};
	}
}