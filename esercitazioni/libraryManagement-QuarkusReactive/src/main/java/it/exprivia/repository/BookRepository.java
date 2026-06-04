package it.exprivia.repository;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import it.exprivia.models.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class BookRepository implements PanacheRepositoryBase<Book, String> {


    // ricerca con paginazione
    public Uni<List<Book>> findByAuthor(String author, int page, int size) {
        return find("lower(author) like lower(?1)", "%" + author + "%")
                .page(page, size)
                .list();
    }


    // preparazione della query per la ricerca con paginazione
    public PanacheQuery<Book> findByTitle(String title) {
        // lower significa che la ricerca è case-sensitive, a prescindere dalle maiuscole e minuscole
        // % significa qualsiasi stringa prima o dopo il titolo
        // %1 significa il primo parametro dopo la query, in questo caso title
        return find("lower(title) like lower(?1)", "%" + title + "%");
    }

    // per unirli ad esempio avrei potuto fare un
    // return find(lower(title) like lower(?1) or lower(author) like lower(?1), "%" + string + "%");
}
