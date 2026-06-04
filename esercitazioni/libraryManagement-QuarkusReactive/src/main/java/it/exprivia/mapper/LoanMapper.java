package it.exprivia.mapper;

import it.exprivia.models.dtos.loan.LoanDTO;
import it.exprivia.models.dtos.loan.LoanResponseDTO;
import it.exprivia.models.entity.Book;
import it.exprivia.models.entity.Loan;
import it.exprivia.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {

    @Mapping(target = "codiceFiscale", source = "user.codiceFiscale")
    @Mapping(target = "isbn", source = "book.isbn")
    LoanDTO toDTO(Loan loan);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "codiceFiscale", source = "user.codiceFiscale")
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "bookTitle", source = "book.title")
    LoanResponseDTO toResponseDTO(Loan loan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loanDate", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Loan toEntity(User user, Book book);

    List<LoanResponseDTO> toResponseDTOList(List<Loan> loans);

}
