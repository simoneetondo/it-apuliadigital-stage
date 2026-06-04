package it.exprivia.services.impl;

import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import it.exprivia.exception.BookNotFoundException;
import it.exprivia.exception.DuplicatedBookException;
import it.exprivia.mapper.BookMapper;
import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;
import it.exprivia.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements IBook {

    @Inject
    BookRepository repo;
    @Inject
    BookMapper mapper;


    /**
     * da inserire con lo stream perchè se la sessione viene chiusa prima che lo stream finisce
     * scateniamo l'errore session closed.
     * stream si utilizza in questi casi perchè tiene la connessione aperta mentre java scorre sugli elementi
     * con il findAll() invece richiami tutta la lista e chiudi la sessione
     **/
    @Transactional
    public List<BookDTO> getBooks(int page, int size) {
        // non ha senso utilizzare il dtoList, convertiamo direttamente nello stream è piu efficiente
        try (var bookStream = repo.findAll().page(Page.of(page, size)).stream()) {
            return bookStream
                    .map(b -> mapper.toDto(b))
                    .toList();

        }
    }


    @Override
    @Transactional
    public List<BookDTO> getBooksByTitle(int page, int size, String title) {
        if (title == null || title.isEmpty())
            return getBooks(page, size);
        try (var bookStream = repo.findPaginatedByTitle(page, size, title).stream()) {
            return bookStream
                    .map(b -> mapper.toDto(b))
                    .toList();
        }
    }


    @Override
    public BookDTO getBookByIsbn(String isbn) {
        return repo.findByIdOptional(isbn)
                .map(b -> mapper.toDto(b))
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        repo.findByIdOptional(bookDTO.isbn()).ifPresent(s -> {
            throw new DuplicatedBookException(bookDTO.isbn());
        });


        Book book = mapper.toEntity(bookDTO);
        repo.persist(book);
        return mapper.toDto(book);

    }

    @Transactional
    @Override
    public BookDTO updateBook(String isbn, BookDTO bookDTO) {
        Book book = repo.findByIdOptional(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));


        mapper.updateEntityFromDto(bookDTO, book);
        return mapper.toDto(book);
    }

    @Transactional
    @Override
    public void deleteBook(String isbn) {
        if (!repo.deleteById(isbn)) {
            throw new BookNotFoundException(isbn);
        }

    }


    public boolean checkDb() {
        return repo.count() > 0;
    }


    @Override
    public Uni<String> test() {
        System.out.println("Inizio");
        Uni<String> messaggio = Uni.createFrom().item("hello, world!")
                .onItem().transform(item -> item + " transformed")
                .onItem().transform(item -> item + " again")
                .onItem().transform(item -> item.toUpperCase());
        //
        //        messaggio.subscribe().with(item -> System.out.println("Messaggio ricevuto: " + item));
        System.out.print("Fine");
        return messaggio;
    }


    @Override
    public Uni<String> test1(String nome) {
        return Uni.createFrom().item(nome)
                .onItem().transform(i -> "Benvenuto " + i)
                .onItem().transform(String::toUpperCase);
    }

    @Override
    public Uni<Book> getBookByIsbnAsynch(String isbn) {
        return Uni.createFrom().item(() -> repo.findById(isbn))
                .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn));
    }


}
