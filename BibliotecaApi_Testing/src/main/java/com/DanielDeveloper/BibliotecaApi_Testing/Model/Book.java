package com.DanielDeveloper.BibliotecaApi_Testing.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.text.DateFormat;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;
     @NotBlank(message = "El autor no puede estar vacio")
    private String autor;

    private Integer anioDePublicacion;

    @Min(value = 0, message = "El stock no puede ser negativo")
     private Integer stock;



}
