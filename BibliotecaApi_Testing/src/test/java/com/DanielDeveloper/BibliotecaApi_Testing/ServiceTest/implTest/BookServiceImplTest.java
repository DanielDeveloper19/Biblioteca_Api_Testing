package com.DanielDeveloper.BibliotecaApi_Testing.ServiceTest.implTest;


import com.DanielDeveloper.BibliotecaApi_Testing.Model.Book;
import com.DanielDeveloper.BibliotecaApi_Testing.Repository.BookRepository;
import com.DanielDeveloper.BibliotecaApi_Testing.Service.BookService;
import com.DanielDeveloper.BibliotecaApi_Testing.Service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    Book book;
    Book bookAactualizar;

@BeforeEach
public void setUp() {
book = Book.builder().titulo("El Varón").autor("Daniel").anioDePublicacion(2023).stock(10).build();

bookAactualizar = Book.builder().titulo("El Cabrón").autor("Camilo").anioDePublicacion(2040).stock(15).build();

}


    @Test
    void createBook_WhenBookIsValid_SavesBook() {
        // Given

        // When
        bookService.createBook(book);

        // Then
        verify(bookRepository, times(1)).save(book);
    }



    @Test
    void createBook_WhenRepositoryFails_ThrowsException() {

        when(bookRepository.save(book)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookService.createBook(book));
    }

    @Test
    void createBook_VerifiesRepositoryCall() {

    // Act
        bookService.createBook(book);

        // Assert
        verify(bookRepository, times(1)).save(book);}

   @Test
   public void createBook_WhenBookIsNull_ThrowsIllegalArgumentException() {

    assertThrows(IllegalArgumentException.class, () -> bookService.createBook(null));

   }


    //Hasta aquí los test del create


    @Test
    public void updateBook_WhenBookIsNotFoundInBd_SavesBook() {

    when(bookRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

    Long id = 1L;

    bookService.updateBook(id, bookAactualizar);

        verify(bookRepository, times(1)).save(bookAactualizar);}

    @Test
    public void updateBook_WhenBookBDIsPresent_UpdatesBook() {
        Long id = 1L;
        // 1. Libro existente (simulado en la BD)
        Book libroExistente = new Book();
        libroExistente.setId(id);
        libroExistente.setTitulo("Titulo original");
        libroExistente.setAutor("Autor original");
        libroExistente.setAnioDePublicacion(2023);
        libroExistente.setStock(10);


        // 2. Datos de actualización (solo algunos campos)
        Book datosActualizados = new Book();
        datosActualizados.setTitulo("Titulo nuevo");  // Campo modificado
        datosActualizados.setAutor(null);            // Campo no modificado

        // 3. Simulamos que findById devuelve el libro existente
        when(bookRepository.findById(id)).thenReturn(Optional.of(libroExistente));

        // 4. Ejecutamos el método
        bookService.updateBook(id, datosActualizados);

        // 5. Verificamos:
        // - Que se guardó el libro existente (NO datosActualizados)
        verify(bookRepository, times(1)).save(libroExistente);

        // - Que los campos se actualizaron correctamente
        assertEquals("Titulo nuevo", libroExistente.getTitulo());  // Campo actualizado
        assertEquals("Autor original", libroExistente.getAutor());                    // Campo no modificado
    }



    //hasta aquí los test del update

    @Test
    public void getBookById_WhenBookExists_ReturnsBook() {
        Long id = 1L;
        Book mockBook = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(mockBook));

        Optional<Book> result = bookService.getBookById(id);

        assertTrue(result.isPresent());
        assertEquals(mockBook, result.get());
    }

    @Test
    public void getBookById_WhenBookNotExists_ReturnsEmpty() {
        Long id = 999L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(id);

        assertFalse(result.isPresent());
    }

//hasta aquí los test del getBookById

    @Test
    public void deleteBook_WhenIdIsValid_CallsRepositoryDelete() {
        Long id = 1L;

        bookService.deleteBook(id);

        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteBook_WhenIdDoesNotExist_DoesNotThrow() {
        Long nonExistentId = 999L;

        // Simula que el repositorio no hace nada (deleteById no lanza excepción por defecto)
        doNothing().when(bookRepository).deleteById(nonExistentId);

        // Verifica que no hay excepciones
        assertDoesNotThrow(() -> bookService.deleteBook(nonExistentId));

        verify(bookRepository, times(1)).deleteById(nonExistentId);
    }

    //hasta aquí los test del delete


    @Test
    public void getAllBooks_WhenBooksExist_ReturnsBooks() {

    //Given
    List<Book> books = List.of( new Book(1L, "El Principito", "Antoine", 1943, 10), // Libro con datos reales
            new Book(2L, "1984", "Orwell", 1949, 5));

    when(bookRepository.findAll()).thenReturn(books);

    //When
        List<Book> booksEncontrados = bookService.getAllBooks();

        //Then
assertFalse(booksEncontrados.isEmpty());
assertEquals(books.size(), booksEncontrados.size());
verify(bookRepository, times(1)).findAll(); // ¿Se llamó al repositorio
        assertEquals(books.get(0).getTitulo(), booksEncontrados.get(0).getTitulo());

    }

    @Test
    public void getAllBooks_WhenNoBooksExist_ReturnsEmptyList() {

    //Given
    when(bookRepository.findAll()).thenReturn(List.of());

    //When
       List<Book> lista = bookService.getAllBooks();

       //Then
       assertTrue(lista.isEmpty());
       verify(bookRepository, times(1)).findAll();

}



}


