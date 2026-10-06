package com.imdb.movie_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FilmeCreateDTO(
        @NotBlank String titulo,
        @NotBlank String diretor,
        @NotBlank String genero,
        @NotEmpty List<String> atores
) {}
