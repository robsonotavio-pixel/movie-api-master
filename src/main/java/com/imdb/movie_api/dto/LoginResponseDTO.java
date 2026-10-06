package com.imdb.movie_api.dto;

public record LoginResponseDTO(
    String token,
    String tipo
) {}
