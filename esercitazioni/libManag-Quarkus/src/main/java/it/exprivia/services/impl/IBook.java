package it.exprivia.services.impl;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;

import java.util.List;

public interface IBook {
    public List<BookDTO> getBooks(int page, int size);
    public List<BookDTO>getBooksByTitle(int page, int size, String title);
    public BookDTO getBookByIsbn(String isbn);
    public BookDTO createBook(BookDTO bookDTO);
    public BookDTO updateBook(String id, BookDTO bookDTO);
    public void deleteBook(String isbn);

    public Uni<String> test();
    public Uni<String> test1(String nome);
    public Uni<Book> getBookByIsbnAsynch(String isbn);

}
