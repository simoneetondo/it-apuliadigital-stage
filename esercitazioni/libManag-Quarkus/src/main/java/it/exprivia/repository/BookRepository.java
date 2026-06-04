package it.exprivia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import it.exprivia.models.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class BookRepository implements PanacheRepositoryBase <Book, String> {

    // con la i prima del like ti fa l'ignore case, quindi non fa distinzione tra maiuscole e minuscole
    public List<Book> findPaginatedByTitle(int page, int size, String title) {
        return find ( "title ilike ?1", "%" + title + "%")
                .page(Page.of(page, size))
                .list();
    }

    public List<Book> findByAuthor(String author){
        return find("author = ?1", author)
                .list();
    }



}
