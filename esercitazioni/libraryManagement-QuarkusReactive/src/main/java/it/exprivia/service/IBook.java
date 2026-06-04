package it.exprivia.service;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.book.BookCreateRequest;
import it.exprivia.models.dtos.book.BookDTO;
import it.exprivia.models.dtos.book.BookStockUpdateRequest;
import it.exprivia.models.dtos.PagedResponse;

import java.util.List;

public interface IBook {

    public Uni<BookDTO> getBookByIsbn(String isbn);
    public Uni<List<BookDTO>> getBooks(int page, int size);
    public Uni<BookDTO> createBook(BookCreateRequest newBook);
    public Uni<BookDTO> updateBook(String isbn, BookDTO updatedBook);
    public Uni<BookDTO> deleteBook(String isbn);

    public Uni<List<BookDTO>> searchBooksByAuthor(String author, int page, int size);
    public Uni<PagedResponse<BookDTO>> searchBooksByTitle(String title, int page, int size);

    public Uni<BookDTO> addStock(String isbn, BookStockUpdateRequest request);
    public Uni<BookDTO> removeStock(String isbn, BookStockUpdateRequest request);

}
