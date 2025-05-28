package com.DanielDeveloper.BibliotecaApi_Testing.Repository;

import com.DanielDeveloper.BibliotecaApi_Testing.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    void deleteAll();

    Book findByTitulo(String titulo);

}
