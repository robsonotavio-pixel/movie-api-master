package com.imdb.movie_api.dto;

public record AdministradorResponseDTO(
              Long id,
              String nome,
              String email,
              Boolean ativo
) {
}            