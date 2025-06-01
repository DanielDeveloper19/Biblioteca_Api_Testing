package com.DanielDeveloper.BibliotecaApi_Testing.Controllers;


import com.DanielDeveloper.BibliotecaApi_Testing.DTO.BookDTO;
import com.DanielDeveloper.BibliotecaApi_Testing.Model.Book;
import com.DanielDeveloper.BibliotecaApi_Testing.Service.impl.BookServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class BookController {


    @Autowired
    private BookServiceImpl bookService;

@PostMapping("/book/save")
    public ResponseEntity<?> createBook(@Valid  @RequestBody BookDTO bookDTO) {

    try{
        Book book = Book.builder()
                .titulo(bookDTO.getTitulo())
                .autor(bookDTO.getAutor())
                .anioDePublicacion(bookDTO.getAnioDePublicacion())
                .stock(bookDTO.getStock()).build();
        bookService.createBook(book);
        return ResponseEntity.created(null).build();

    }catch (Exception e){
log.error("Error al crear libro {}",e.getMessage());
return ResponseEntity.internalServerError().build();

}
}

@GetMapping("/book/id/{id}" )
    public ResponseEntity<?> getBookById(@PathVariable Long id) {

    Optional<Book> book = bookService.getBookById(id);

    if (book.isPresent()) {
        Book book1 = book.get();

        BookDTO bookDTO = BookDTO.builder()
                .id(book1.getId())
                .titulo(book1.getTitulo())
                .autor(book1.getAutor())
                .anioDePublicacion(book1.getAnioDePublicacion())
                .stock(book1.getStock())
                .build();
        return ResponseEntity.ok(bookDTO);

    } else {
        return ResponseEntity.notFound().build();
    }
}

@GetMapping("/book/all")
    public ResponseEntity<?> getAllBooks() {

    try {
        List<Book> books = bookService.getAllBooks();

if (books.isEmpty()) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
}
        List<BookDTO> bookDTOS = books.stream()
                .map(book -> BookDTO.builder()
                        .id(book.getId())
                        .titulo(book.getTitulo())
                        .autor(book.getAutor())
                        .anioDePublicacion(book.getAnioDePublicacion())
                        .stock(book.getStock())
                        .build()).toList();
        return ResponseEntity.ok(bookDTOS);
    }catch (Exception e){
log.error("Error al obtener todos los libros {}",e.getMessage());
return ResponseEntity.internalServerError().build();

    }




}

@PutMapping("/book/update")
    public ResponseEntity<?> updateBook( @RequestBody BookDTO bookDTO) {

    if(bookDTO.getId() == null ){
        return ResponseEntity.badRequest().build();
    }

    try{
        bookService.updateBook(bookDTO.getId(), Book.builder()
                .titulo(bookDTO.getTitulo())
                .autor(bookDTO.getAutor())
                .anioDePublicacion(bookDTO.getAnioDePublicacion())
                .stock(bookDTO.getStock()).build());

        return ResponseEntity.ok().build();

    }catch (Exception e){
        log.error("Error al actualizar libro {}",e.getMessage());
        return ResponseEntity.internalServerError().build();
    }

}


@DeleteMapping("/book/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);
        return ResponseEntity.ok().build();

}




}
