package com.DanielDeveloper.BibliotecaApi_Testing.Service;

import com.DanielDeveloper.BibliotecaApi_Testing.Model.Book;

import java.util.List;
import java.util.Optional;


public interface BookService {

void createBook(Book book);
Optional<Book> getBookById(Long id);
void updateBook(Long id, Book book);
void deleteBook(Long id);
List<Book> getAllBooks();



}
