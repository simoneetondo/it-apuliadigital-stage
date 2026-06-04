package it.exprivia.models.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import it.exprivia.models.enums.BookStatusEnum.LoanStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "loans")
public class Loan extends PanacheEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    public Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(name = "loan_date", nullable = false)
    public LocalDate loanDate = LocalDate.now();

    @Column(name = "return_date", nullable = false)
    public LocalDate returnDate = LocalDate.now().plusDays(15);

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public LoanStatus status = LoanStatus.ACTIVE;

    public Loan() {
    }

    public Loan(Book book, User user, LocalDate loanDate, LocalDate returnDate, LoanStatus status) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(book, loan.book) && Objects.equals(user, loan.user) && Objects.equals(loanDate, loan.loanDate) && Objects.equals(returnDate, loan.returnDate) && status == loan.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, user, loanDate, returnDate, status);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "book=" + book +
                ", user=" + user +
                ", loanDate='" + loanDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", status=" + status +
                '}';
    }
}
