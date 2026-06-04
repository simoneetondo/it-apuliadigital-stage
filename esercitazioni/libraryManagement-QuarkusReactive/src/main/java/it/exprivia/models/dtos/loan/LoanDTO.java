package it.exprivia.models.dtos.loan;

import it.exprivia.models.enums.BookStatusEnum.LoanStatus;

import java.time.LocalDate;

public record LoanDTO(
         String isbn,
         String codiceFiscale,
         LocalDate loanDate,
         LocalDate returnDate,
         LoanStatus status
) {
}
