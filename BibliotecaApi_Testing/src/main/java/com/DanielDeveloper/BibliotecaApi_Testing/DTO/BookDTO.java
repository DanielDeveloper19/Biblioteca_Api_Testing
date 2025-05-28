package com.DanielDeveloper.BibliotecaApi_Testing.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private Long id;
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;
    @NotBlank(message = "El autor no puede estar vacio")
    private String autor;

    private Integer anioDePublicacion;

    private Integer stock;



}
