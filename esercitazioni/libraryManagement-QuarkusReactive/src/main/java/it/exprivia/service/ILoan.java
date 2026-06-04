package it.exprivia.service;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.PagedResponse;
import it.exprivia.models.dtos.loan.LoanDTO;
import it.exprivia.models.dtos.loan.LoanResponseDTO;
import it.exprivia.models.entity.Loan;

import java.util.List;

public interface ILoan {

    Uni<LoanDTO> createLoan(String codiceFiscale, String isbn);

    Uni<LoanResponseDTO> returnLoan(String codiceFiscale, String isbn);

    Uni<Void> deleteLoan(String codiceFiscale, String isbn);

    Uni<List<LoanResponseDTO>> getLoansByUser(String codiceFiscale);

    Uni<PagedResponse<LoanResponseDTO>> getAllLoans(int page, int size);
}