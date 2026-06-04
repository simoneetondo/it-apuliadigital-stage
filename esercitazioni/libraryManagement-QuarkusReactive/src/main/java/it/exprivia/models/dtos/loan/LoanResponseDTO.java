package it.exprivia.models.dtos.loan;

import it.exprivia.models.enums.BookStatusEnum.LoanStatus;

import java.time.LocalDate;

public record LoanResponseDTO(
        String id,
        String codiceFiscale,
        String isbn,
        String bookTitle,
        LocalDate loanDate,
        LocalDate returnDate,
        LoanStatus status
) {
}
