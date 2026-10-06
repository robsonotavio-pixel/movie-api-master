package com.imdb.movie_api.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdministradorUpdateDTO(
    
       @NotBlank String nome,
       @NotBlank @Email String email
) {
} 
