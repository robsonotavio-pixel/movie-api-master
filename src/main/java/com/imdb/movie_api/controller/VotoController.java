package com.imdb.movie_api.controller;

import com.imdb.movie_api.dto.VotoCreateDTO;
import com.imdb.movie_api.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filmes/{filmeId}/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public void votar(@PathVariable Long filmeId, @RequestBody @Valid VotoCreateDTO dto, Authentication authentication) {
        votoService.votar(filmeId, authentication.getName(), dto);
    }
}
