package it.exprivia.mapper;

import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface BookMapper {

    Book toEntity(BookDTO bookDTO);

    BookDTO toDto(Book book);

    List<BookDTO> toDtoList(List<Book> books);

    Book updateEntityFromDto(BookDTO dto, @MappingTarget Book entity);

}
