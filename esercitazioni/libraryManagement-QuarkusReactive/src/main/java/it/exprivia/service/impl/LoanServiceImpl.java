package it.exprivia.service.impl;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import it.exprivia.exception.book.BookNotFoundException;
import it.exprivia.exception.user.UserNotFoundException;
import it.exprivia.mapper.LoanMapper;
import it.exprivia.models.dtos.PagedResponse;
import it.exprivia.models.dtos.loan.LoanDTO;
import it.exprivia.models.dtos.loan.LoanResponseDTO;
import it.exprivia.models.entity.Book;
import it.exprivia.models.entity.Loan;
import it.exprivia.models.entity.User;
import it.exprivia.models.enums.BookStatusEnum.LoanStatus;
import it.exprivia.repository.BookRepository;
import it.exprivia.repository.LoanRepository;
import it.exprivia.repository.UserRepository;
import it.exprivia.service.ILoan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanServiceImpl implements ILoan {

    @Inject
    LoanRepository loanRepo;

    @Inject
    LoanMapper mapper;

    @Inject
    UserRepository userRepo;
    @Inject
    BookRepository bookRepo;


    /**
     * crea un prestito per un utente ed un libro specifiacto, se l'utente e il libro non esistono restituisce un eccezione
     *
     * @param codiceFiscale - il codice fiscale dell'utente che vuole prendere in prestito il libro
     * @param isbn          - l'isbn del libro che l'utente vuole prendere in prestito
     * @return il dto del prestito creato
     * @throws BookNotFoundException se il libro con l'isbn specificato inizialmente non esiste
     * @throws UserNotFoundException se l'utente con il codice fiscale specificato non esiste
     * @throws RuntimeException      se esiste già un prestito attivo per quell'utente e quel libro
     */
    @WithTransaction
    @Override
    public Uni<LoanDTO> createLoan(String codiceFiscale, String isbn) {
        return userRepo.findById(codiceFiscale)
                .onItem().ifNull().failWith(() -> new UserNotFoundException(codiceFiscale))
                .chain(user -> bookRepo.findById(isbn)
                        .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn))
                        .chain(book -> loanRepo.findByIsbnAndCodiceFiscale(user, book)
                                .onItem().ifNotNull().failWith(() -> new RuntimeException(
                                        "Loan already exists for user: " + codiceFiscale + " and isbn: " + isbn))
                                .replaceWith(mapper.toEntity(user, book))
                                .chain(newLoan -> loanRepo.persist(newLoan))
                                .map(savedLoan -> mapper.toDTO(savedLoan))
                        )
                );
    }

    @WithTransaction
    @Override
    public Uni<LoanResponseDTO> returnLoan(String codiceFiscale, String isbn) {
        return loanRepo.findActiveLoan(codiceFiscale, isbn)
                .onItem().ifNull().failWith(() -> new RuntimeException("..."))
                .chain(loan -> {
                    loan.setStatus(LoanStatus.RETURNED);
                    loan.setReturnDate(LocalDate.now());
                    return loanRepo.persist(loan);
                })
                .map(updatedLoan -> mapper.toResponseDTO(updatedLoan));
    }

    @Override
    public Uni<Void> deleteLoan(String codiceFiscale, String isbn) {
        return null;
    }

    // con il repo ci facciamo restituire una lista che non può mai essere vuota,
    // se è vuota lanciamo ececzione, e poi mettiamo i controlli sul prestito.
    @WithSession
    @Override
    public Uni<List<LoanResponseDTO>> getLoansByUser(String codiceFiscale) {
        return loanRepo.findByCodiceFiscaleList(codiceFiscale)
                .onItem().transform(loans -> {
                    if (loans.isEmpty())
                        throw new RuntimeException("No loans found for user with codice fiscale: " + codiceFiscale);
                    return loans.stream()
                            .map(loan -> mapper.toResponseDTO(loan))
                            .collect(Collectors.toList());
                });
    }

    @WithSession
    @Override
    public Uni<PagedResponse<LoanResponseDTO>> getAllLoans(int page, int size) {
        PanacheQuery<Loan> query = loanRepo.findAll().page(page, size);
        return query.list()
                .chain(loans -> query.count()
                        .map(total ->
                                new PagedResponse<>(
                                        mapper.toResponseDTOList(loans),
                                        total,
                                        total / size,
                                        page,
                                        size
                                )
                        )
                );
    }
}

