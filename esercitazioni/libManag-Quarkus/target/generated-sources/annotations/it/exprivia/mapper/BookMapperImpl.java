package it.exprivia.mapper;

import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-16T13:05:47+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Singleton
@Named
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toEntity(BookDTO bookDTO) {
        if ( bookDTO == null ) {
            return null;
        }

        Book book = new Book();

        book.setTitle( bookDTO.title() );
        book.setAuthor( bookDTO.author() );
        book.setIsbn( bookDTO.isbn() );
        book.setPublicationYear( bookDTO.publicationYear() );
        book.setGenre( bookDTO.genre() );

        return book;
    }

    @Override
    public BookDTO toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        String isbn = null;
        String title = null;
        String author = null;
        int publicationYear = 0;
        String genre = null;

        isbn = book.getIsbn();
        title = book.getTitle();
        author = book.getAuthor();
        publicationYear = book.getPublicationYear();
        genre = book.getGenre();

        Integer stock = null;

        BookDTO bookDTO = new BookDTO( isbn, title, author, publicationYear, genre, stock );

        return bookDTO;
    }

    @Override
    public List<BookDTO> toDtoList(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookDTO> list = new ArrayList<BookDTO>( books.size() );
        for ( Book book : books ) {
            list.add( toDto( book ) );
        }

        return list;
    }

    @Override
    public Book updateEntityFromDto(BookDTO dto, Book entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.title() != null ) {
            entity.setTitle( dto.title() );
        }
        if ( dto.author() != null ) {
            entity.setAuthor( dto.author() );
        }
        if ( dto.isbn() != null ) {
            entity.setIsbn( dto.isbn() );
        }
        entity.setPublicationYear( dto.publicationYear() );
        if ( dto.genre() != null ) {
            entity.setGenre( dto.genre() );
        }

        return entity;
    }
}
