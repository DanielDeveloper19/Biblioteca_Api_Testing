package com.DanielDeveloper.BibliotecaApi_Testing.ControllerTest;


import com.DanielDeveloper.BibliotecaApi_Testing.Controllers.BookController;
import com.DanielDeveloper.BibliotecaApi_Testing.Model.Book;
import com.DanielDeveloper.BibliotecaApi_Testing.Repository.BookRepository;
import com.DanielDeveloper.BibliotecaApi_Testing.Service.impl.BookServiceImpl;
import jakarta.persistence.EntityManager;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class BookControllerTest {

 @Autowired
 private MockMvc mockMvc;

 @Autowired
 private BookServiceImpl bookService;

 @Autowired
 private BookRepository bookRepository;

 @Autowired
 EntityManager entityManager;


 @BeforeEach
public void setUp() throws Exception {
    bookRepository.deleteAll();
}


@Test
@Transactional
public void getAllBooksTest_WhenTheresNotBooks_IsNoContent() throws Exception {

 //En principio no debería devolver nada
 mockMvc.perform(get("/book/all"))
         .andExpect(status().isNoContent());
}

@Test
@Transactional
public void getAllBooksTest_WhenTheresBooks_IsOk() throws Exception {

 //Ahora agregamos un libro y vemos que pasa
 bookRepository.save(com.DanielDeveloper.BibliotecaApi_Testing.Model.Book.builder()
         .titulo("El Cabrón").autor("Camilo").anioDePublicacion(2040).stock(15).build());

 mockMvc.perform(get("/book/all"))
         .andDo(print())
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].titulo").value("El Cabrón"))
         .andExpect(jsonPath("$[0].autor").value("Camilo"))
         .andExpect(jsonPath("$[0].anioDePublicacion").value(2040))
         .andExpect(jsonPath("$[0].stock").value(15));
}

//Hasta aquí el getAllBooks


 @Test
 @Transactional
 public void createBook_invalidObjectSaved_shouldReturnBadRequestWhenInvalid() throws Exception {
  String invalidBookJson = """
        {
            "titulo": "",  // Inválido
            "autor": null, // Inválido
            "anioDePublicacion": -1,
            "stock": -5
        }
        """;

  mockMvc.perform(post("/book/save")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(invalidBookJson))
          .andDo(print())
          .andExpect(status().isBadRequest());

//verificar que no se guardó ningún registro en la bd


assertThat(bookRepository.findAll()).isEmpty();


 }

@Test
 @Transactional
 public void createBook_validObjectSaved_shouldReturnCreated() throws Exception {
 String validBookJson = """
        {
            "titulo": "El Cabrón",
            "autor": "Camilo",
            "anioDePublicacion": 2040,
            "stock": 15
        }
 """;
 mockMvc.perform(post("/book/save")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(validBookJson))
         .andDo(print())
         .andExpect(status().isCreated());

 assertThat(bookRepository.findAll()).hasSize(1);

 Book book = bookRepository.findAll().get(0);

 assertEquals("El Cabrón", book.getTitulo());
assertEquals("Camilo", book.getAutor());
assertEquals(2040, book.getAnioDePublicacion());
assertEquals(15, book.getStock());
 }
 //Hasta aquí el createBook

 @Test
 @Transactional
 public void getBookById_WhenBookIdNoExists_ReturnNotFound() throws Exception {

  Long nonExistentId = 999L;
  mockMvc.perform(get("/book/id/{id}", nonExistentId))
          .andExpect(status().isNotFound());
 }

 @Test
 @Transactional
 public void getBookById_WhenBookIdExists_ReturnOk() throws Exception {

  Book book = Book.builder()
           .titulo("El Cabrón")
           .autor("Camilo")
           .anioDePublicacion(2040)
           .stock(15)
           .build();

  Book savedBook = bookRepository.save(book);

  mockMvc.perform(get("/book/id/{id}", savedBook.getId()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.titulo").value("El Cabrón"))
          .andExpect(jsonPath("$.autor").value("Camilo"))
          .andExpect(jsonPath("$.anioDePublicacion").value(2040))
          .andExpect(jsonPath("$.stock").value(15));

 }






}









