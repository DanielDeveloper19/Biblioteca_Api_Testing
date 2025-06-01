package com.DanielDeveloper.BibliotecaApi_Testing.Service.impl;

import com.DanielDeveloper.BibliotecaApi_Testing.Model.Book;
import com.DanielDeveloper.BibliotecaApi_Testing.Repository.BookRepository;
import com.DanielDeveloper.BibliotecaApi_Testing.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void createBook(Book book) {

        if(book == null){
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }

bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookById(Long id){
        return bookRepository.findById(id);
    }

    @Override
    public void updateBook(Long id, Book book) {


        Book bookEncontrado = bookRepository.findById(id).orElse(null);

try {
    if (book.getStock() < 0 || book.getAnioDePublicacion() < 0) {
        throw new IllegalArgumentException("Los campos de stock y anio de publicación no pueden ser negativos");
    }
}catch (NullPointerException e){}



        if (bookEncontrado != null) {

            if(book.getTitulo() != null){
                bookEncontrado.setTitulo(book.getTitulo());
            }
            if(book.getAutor() != null){
                bookEncontrado.setAutor(book.getAutor());
            }
            if(book.getAnioDePublicacion() != null){
                bookEncontrado.setAnioDePublicacion(book.getAnioDePublicacion());
            }
            if(book.getStock() != null ){

                bookEncontrado.setStock(book.getStock());
            }

            bookRepository.save(bookEncontrado);
        } else {

            //Tendría que testear todos estos caminos
            if (book.getTitulo().isBlank() || book.getAutor().isBlank() || book.getAnioDePublicacion() == null || book.getStock() == null){
                throw new IllegalArgumentException("si el libro no existe debe tener todos sus campos completos para poder ser guardado como un libro nuevo");
            }
            bookRepository.save(book);
        }

    }

    @Override
    public void deleteBook(Long id) {
bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {

       return  bookRepository.findAll();

    }

    // Hasta aquí los métodos de la getAllBooks
}
