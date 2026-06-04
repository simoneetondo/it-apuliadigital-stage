package it.exprivia;


import io.quarkus.test.junit.QuarkusTest;
import it.exprivia.exception.BookNotFoundException;
import it.exprivia.exception.DuplicatedBookException;
import it.exprivia.mapper.BookMapper;
import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;
import it.exprivia.repository.BookRepository;
import it.exprivia.services.impl.BookServiceImpl;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class GreetingResourceTest {

    @Inject
    BookServiceImpl service;

    @Inject
    private BookRepository repo;

    @Inject
    private BookMapper mapper;

    @BeforeEach
    @Transactional
    void setup() {
        repo.deleteAll();
    }

    @Test
    void should_ThrowException_When_BookNotFound() {
        // given
        String isbn = "123-456";
        // when

        // then
        assertThrows(BookNotFoundException.class, () -> {
            service.getBookByIsbn(isbn);
        });


    }

    @Test
    void should_ReturnBoock_When_CorrectIsbn() {
        // given
        String isbn = "123-456";
        // when

        // then
        assertThrows(BookNotFoundException.class, () -> {
            service.getBookByIsbn(isbn);
        });


    }

    @Transactional
    @Test
    void should_ReturnRightList() {
        Book book = new Book();
        book.setIsbn("123");
        book.persist();

        List<BookDTO> result = service.getBooks(0, 10);

        assertEquals(1, result.size());

    }

    @Transactional
    @Test
    void should_CreateBook_WhenCorrectData() {
        //given
        BookDTO bookDTO = new BookDTO("123", "", "", 1999, "",2);

        //when
        BookDTO result = service.createBook(bookDTO);

        //then
        assertNotNull(result);

        Book savedBook = repo.findById("123");
        assertNotNull(savedBook);
    }

    @Transactional
    @Test
    void should_ThrowException_WhenBookAlreadyExistWithId() {
        BookDTO bookDTO = new BookDTO("123", "", "", 1999, "",2);
        Book existing = new Book();
        existing.setIsbn("123");
        existing.persist();

        assertThrows(DuplicatedBookException.class, () -> {
            service.createBook(bookDTO);
        });
    }

    @Transactional
    @Test
    void should_Delete_WhenDataExistaAndCorrectId() {
        //given
        Book book = new Book();
        book.setIsbn("123");
        book.persist();
        // when
        service.deleteBook("123");
        // then
        Book deletedBook = repo.findById("123");
        assertNull(deletedBook, "Il libro  con isbn: " + book.getIsbn() + " è stato eliminato");
    }

    @Transactional
    @Test
    void should_ThrowException_WhenInvalidId() {
        String isbn = "123";

        assertThrows(BookNotFoundException.class, () -> {
            service.deleteBook(isbn);
        });
    }


    @Transactional
    @Test
    void should_UpdateBook_WhenCorrectData() {
        // given
        Book existing = new Book();
        existing.setIsbn("123");
        existing.setTitle("Titanic");
        existing.persist();

        BookDTO updating = new BookDTO("123", "Titanic v2.0", "", 1999, "",2);

        //when
        BookDTO result = service.updateBook(updating.isbn(), updating);
        //then
        assertNotNull(result);
        assertEquals(updating.title(), result.title());
    }

    @Transactional
    @Test
    void should_NotUpdateBook_WhenCorrectData() {
        // given
        Book existing = new Book();
        existing.setIsbn("123");
        existing.setTitle("Titanic");
        existing.persist();

        BookDTO updating = new BookDTO("456", "Titanic v2.0", "", 1999, "",2);

        //when
        assertThrows(BookNotFoundException.class, () -> {
            service.updateBook(updating.isbn(), updating);
        });    //then

    }


}
