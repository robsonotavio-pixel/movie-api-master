package com.imdb.movie_api.dto;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Boolean ativo 
) {
}