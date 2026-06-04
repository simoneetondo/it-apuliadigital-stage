package it.exprivia.mapper;


import it.exprivia.models.dtos.book.BookCreateRequest;
import it.exprivia.models.dtos.book.BookDTO;
import it.exprivia.models.entity.Book;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jakarta",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    @Mapping(target = "status", ignore = true)
    Book toCreateRequest(BookCreateRequest bookDTO);

    BookDTO toDTO(Book book);

    List<BookDTO> toDTOList(List<Book> books);

    @Mapping(target = "status", ignore = true)
    Book toEntity(BookDTO bookDTO);

    @Mapping(target = "status", ignore = true)
    void updateEntityFromDTO(BookDTO bookDTO, @MappingTarget Book book);
}
