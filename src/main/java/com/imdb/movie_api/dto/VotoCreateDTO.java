package com.imdb.movie_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VotoCreateDTO(
        @NotNull @Min(0) @Max(4) Integer nota
) {}
