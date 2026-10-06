package com.imdb.movie_api.controller;

import com.imdb.movie_api.dto.FilmeCreateDTO;
import com.imdb.movie_api.dto.FilmeResponseDTO;
import com.imdb.movie_api.service.FilmeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public FilmeResponseDTO criar(@RequestBody @Valid FilmeCreateDTO dto) {
        return filmeService.criar(dto);
    }

    @GetMapping
    public Page<FilmeResponseDTO> listar(
            @RequestParam(required = false) String diretor,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String ator,
            Pageable pageable
    ) {
        return filmeService.listar(diretor, nome, genero, ator, pageable);
    }

    @GetMapping("/{id}")
    public FilmeResponseDTO detalhar(@PathVariable Long id) {
        return filmeService.detalhar(id);
    }
}
