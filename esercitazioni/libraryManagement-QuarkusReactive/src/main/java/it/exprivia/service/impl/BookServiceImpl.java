package it.exprivia.service.impl;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import it.exprivia.exception.book.BookNotFoundException;
import it.exprivia.exception.book.DuplicateBookException;
import it.exprivia.exception.book.InsufficientStockException;
import it.exprivia.mapper.BookMapper;
import it.exprivia.models.dtos.book.BookCreateRequest;
import it.exprivia.models.dtos.book.BookDTO;
import it.exprivia.models.dtos.book.BookStockUpdateRequest;
import it.exprivia.models.dtos.PagedResponse;
import it.exprivia.models.entity.Book;
import it.exprivia.models.enums.BookStatusEnum.BookStatus;
import it.exprivia.repository.BookRepository;
import it.exprivia.service.IBook;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements IBook {

    @Inject
    BookRepository repo;

    @Inject
    BookMapper mapper;


    /**
     * Recupera un libro tramite ISBN.
     *
     * @param isbn Il codice ISBN validato.
     * @return Un {@link Uni} che viene trasformato in un  {@link BookDTO}  se trovato, altrimenti restituisce un errore.
     * @throws BookNotFoundException se il libro non esiste.
     */
    @Retry(maxRetries = 3, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5, delay = 1000)
    // fallback? solo per le chiamate get
    @WithSession
    @Override
    public Uni<BookDTO> getBookByIsbn(String isbn) {
        // 1. ricerca del libro per isbn
        return repo.findById(isbn)
                // 2. se non lo troviamo falliamo e restituiamo un exception
                .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn))
                // .onItem().transform(b -> mapper.toDTO(b));
                // 3. se lo troviamo, prendiamo l'oggetto, lo mappiamo per restituire un dto
                .map(b -> mapper.toDTO(b));

    }

    /**
     * Recupera una lista di libri con paginazione.
     *
     * @param page L'indice della pagina da recuperare (0-based).
     * @param size Il numero di elementi per pagina.
     * @return Un {@link Uni} contenente la lista dei {@link BookDTO}.
     * Restituisce una lista vuota se non ci sono risultati per la pagina richiesta.
     */
    @Override
    // @WithSession necessaria per le operazioni di lettura, più leggera di transactional
    @WithSession
    @Retry(maxRetries = 3, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5, delay = 1000)
    public Uni<List<BookDTO>> getBooks(int page, int size) {
        PanacheQuery<Book> query = repo.findAll().page(page, size);
        return query.list()
                .map(b -> mapper.toDTOList(b));
    }


    /**
     * Crea un nuovo libro da aggiungere nel db.
     *
     * @param newBook Il DTO del libro da creare, con validazione.
     * @return Un Uni contenente il libro creato, se il libro esiste già restituiamo un errore.
     * @throws DuplicateBookException
     */
    @Override
    @Retry(maxRetries = 3, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5, delay = 1000)
    public Uni<BookDTO> createBook(BookCreateRequest newBook) {
        return Panache.withTransaction(() ->
                repo.findById(newBook.isbn())
                        .onItem().ifNotNull().failWith(() -> new DuplicateBookException(newBook.isbn()))
                        // quà la prossima operazione è asincrona, utilizziamo il repo perstist che restituisce un uni
                        // quindi usiamo il chain per concatenare l'operazione
                        .chain(() -> {
                            Book savedBook = mapper.toCreateRequest(newBook);
                            savedBook.setStatus(savedBook.getStock() == 0 ? BookStatus.OUT_OF_STOCK : BookStatus.AVAILABLE);
                            return repo.persist(savedBook)
                                    // restituiendo un uni, se dovesse andare a buon fine il salvataggio
                                    // dobbiamo mapparlo in un DTO, ed utilizziamo il map
                                    .map(s -> mapper.toDTO(s));
                        })
        );
    }


    // -- VERSIONE CON LA TRANSACTIONAL PIU SNELLA SENZA LE GRAFFE DOPO CHAIN
//    @Override
//    public Uni<BookDTO> createBook(BookDTO newBook) {
//        return Panache.withTransaction(() ->
//                repo.findById(newBook.isbn())
//                        .onItem().ifNotNull().failWith(() -> new RuntimeException("Book already exists"))
//                        .chain(() ->
//                                repo.persist(mapper.toEntity(newBook)))
//                        .map(s -> mapper.toDTO(s))
//        );
//    }


    /**
     * {@code Panache.withTransaction } e {@code @WithTransaction } svolgono esattamente la stessa cosa
     * solo in metodi dove ci sono operazioni ad alta concorrenza si preferisce utilizzare Panache.
     * la transazione di Panache garantisce che l'intera pipeline dalla ricerca al salvataggio avvenga nello stesso
     * "contesto di persistenza"
     *
     * <p>
     * <b>Note: la versione programmatica ti permette di gestire esattamente dove inizia e finisce l'operazione
     * * in questo modo due operazioni, A e B, non si sovrappongono, se provano a modificare lo stesso libro il db utilizzerà
     * * i suoi meccanismi per far si che i dati rimangano coerenti</b>
     * </p>
     **/
    @Override
    @Retry(maxRetries = 3, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5, delay = 1000)
    public Uni<BookDTO> updateBook(String isbn, BookDTO updatedBook) {
        // 1. apriamo una transazione, se fallisce non cambia nulla nel DB
        return Panache.withTransaction(() ->
                // 2. cerchiamo il libro da isbn
                repo.findById(isbn)
                        // se è null falliamo con un errore
                        .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn))
                        // se lo troviamo usiamo il map perchè la prossima operazione è sincrona e restituisce un oggetto diretto
                        .map(existingBook -> {
                            mapper.updateEntityFromDTO(updatedBook, existingBook);
                            existingBook.setIsbn(isbn);
                            existingBook.setStatus(existingBook.getStock() == 0 ? BookStatus.OUT_OF_STOCK : BookStatus.AVAILABLE);
                            return existingBook;
                        })
                        .map(b -> mapper.toDTO(b))
        );
    }


    /**
     * Elimina un libro dal db.
     *
     * @param isbn
     * @return Se il libro esiste ed è stato cancellato, restituisce il DTO del libro cancellato, altrimenti restituisce un errore.
     */
    @Override
    @Retry(maxRetries = 3, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5, delay = 1000)
    public Uni<BookDTO> deleteBook(String isbn) {
        // 1. Apriamo una transazione: se la cancellazione fallisce, non cambia nulla nel DB
        return Panache.withTransaction(() ->
                // 2. cerchiamo il libro da isbn
                repo.findById(isbn)
                        // 3. se non lo troviamo falliamo con un errore
                        .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn))
                        // 4. se lo troviamo, usiamo il chain perchè la prossima operazione sarà asincrona, restituisce un uni e non un oggetto diretto
                        .chain(book -> repo.delete(book)
                                        // 5. se il db conferma restituisce un Uni<Void>, se la cancellazione va a buon fine, allora restituiamo il DTO del libro cancellato
                                        // lo sostituiamo con il DTO del libro
                                        .replaceWith(mapper.toDTO(book))
                                // il replaceWith può accettare direttamente l'oggetto
                                //  .replaceWith(() -> mapper.toDTO(book))
                        )
        );
    }


    /**
     * Ricerca libri per autora con paginazione
     *
     * @param author
     * @param page
     * @param size
     * @return Una listra di libri che corrispondono all'autore ricercato, se non ci sono risultati restituisce una lista vuota.
     */
    @WithSession
    @Override
    public Uni<List<BookDTO>> searchBooksByAuthor(String author, int page, int size) {
        return repo.findByAuthor(author, page, size)
                .map(b -> mapper.toDTOList(b));
    }


    /**
     * Ricerca libri per titolo con paginazione
     *
     * @param title titolo del libro da cercare
     * @param page  pagina corrente, parte da 0 come convenzione Panache
     * @param size  numero libri per pagina
     * @return Restituisce un oggetto PagedResponse che contiene la lista dei libri trovati,
     * * il numero totale dei libri, il numero totale di pagine, la pagina corrente e la dimensione della pagina
     */
    @WithSession
    @Override
    public Uni<PagedResponse<BookDTO>> searchBooksByTitle(String title, int page, int size) {
        PanacheQuery<Book> query = repo.findByTitle(title).page(page, size);
        return query.list()                                          // prende i libri della pagina
                .flatMap(books                            // quando i libri sono pronti
                        -> query.count()                            // prende il numero totale dei libri
                        .map(total -> new PagedResponse<>(    // quando hai il totale, costruisce un oggetto pagedResponse
                                mapper.toDTOList(books),           // lista dei libri mappata in dto
                                total,                            // totale dei libri
                                total / size,                    // totale pagine (es. 47/5 = 9 pagine)
                                page,                           // pagina corrente (es.0)
                                size                           // dimensione pagina

                        ))
                );
    }

    /**
     * Aggiunge stock ad un libro esistente.
     *
     * @param request contiene l'ISBN del libro e la quantità da aggiungere
     * @return il DTO del libro aggiornato con il nuovo stock e status
     * @throws BookNotFoundException    se il libro con l'ISBN specificato non esiste
     * @throws IllegalArgumentException se la quantità è minore o uguale a 0
     */
    @WithTransaction
    @Override
    public Uni<BookDTO> addStock(String isbn, BookStockUpdateRequest request) {
        return repo.findById(isbn)
                .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn))
                .flatMap(b -> {
                    b.setStock(b.getStock() + request.quantity());
                    b.setStatus(BookStatus.AVAILABLE);
                    return repo.persist(b);
                })
                .map(b -> mapper.toDTO(b));

    }

    /**
     * Rimuove stock ad un libro esistente.
     *
     * @param request contiene l'ISBN del libro e la quantità da rimuovere
     * @return il DTO del libro aggiornato con il nuovo stock e status
     * @throws BookNotFoundException    se il libro con l'ISBN specificato non esiste
     * @throws IllegalArgumentException se la quantità è minore o uguale a 0
     */
    @WithTransaction
    @Override
    public Uni<BookDTO> removeStock(String isbn, BookStockUpdateRequest request) {
        return repo.findById(isbn)
                .onItem().ifNull().failWith(() -> new BookNotFoundException(isbn))
                .flatMap(b -> {
                    if (request.quantity() > b.getStock()) {
                        throw new InsufficientStockException(isbn);
                    }
                    b.setStock(b.getStock() - request.quantity());
                    // la logica
                    if (b.getStock() == 0) {
                        b.setStatus(BookStatus.OUT_OF_STOCK);
                    }
                    return repo.persist(b);
                })
                .map(b -> mapper.toDTO(b));

    }


}




