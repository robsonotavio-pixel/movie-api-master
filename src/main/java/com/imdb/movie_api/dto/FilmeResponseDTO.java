package com.imdb.movie_api.dto;

import java.util.List;

public record FilmeResponseDTO(
        Long id,
        String titulo,
        String diretor,
        String genero,
        List<String> atores,
        Long totalVotos,
        Double mediaVotos
) {}
