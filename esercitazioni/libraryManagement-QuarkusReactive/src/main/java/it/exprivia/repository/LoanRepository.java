package it.exprivia.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import it.exprivia.models.entity.Book;
import it.exprivia.models.entity.Loan;
import it.exprivia.models.entity.User;
import it.exprivia.models.enums.BookStatusEnum.LoanStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LoanRepository implements PanacheRepository<Loan> {


    public Uni<Loan> findByIsbnAndCodiceFiscale(User user, Book book) {
        return find("user = ?1 and book = ?2 and status= ?3", user, book, LoanStatus.ACTIVE).firstResult();
    }

    public Uni<List<Loan>> findByCodiceFiscaleList(String codiceFiscale) {
        return find("user.codiceFiscale = ?1", codiceFiscale).list();
    }

    public Uni<Loan> findByCodiceFiscale(String codiceFiscale) {
        return find("codiceFiscale = ?1", codiceFiscale).firstResult();
    }


    public Uni<Loan> findActiveLoan(String cf, String isbn) {
        return find("user.codiceFiscale and book.isbn and status = ?3", cf, isbn, LoanStatus.ACTIVE).firstResult();
    }

    public Uni<Loan> findByBookId(String bookIsbn) {
        return find("bookIsbn = ?1", bookIsbn).firstResult();
    }
}
