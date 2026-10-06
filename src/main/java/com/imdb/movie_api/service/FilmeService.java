package com.imdb.movie_api.service;

import com.imdb.movie_api.dto.FilmeCreateDTO;
import com.imdb.movie_api.dto.FilmeResponseDTO;
import com.imdb.movie_api.entity.Ator;
import com.imdb.movie_api.entity.Filme;
import com.imdb.movie_api.exception.ResourceNotFoundException;
import com.imdb.movie_api.repository.AtorRepository;
import com.imdb.movie_api.repository.FilmeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final AtorRepository atorRepository;

    public FilmeService(FilmeRepository filmeRepository, AtorRepository atorRepository) {
        this.filmeRepository = filmeRepository;
        this.atorRepository = atorRepository;
    }

    public FilmeResponseDTO criar(FilmeCreateDTO dto) {
        Filme filme = new Filme();
        filme.setTitulo(dto.titulo());
        filme.setDiretor(dto.diretor());
        filme.setGenero(dto.genero());

        Set<Ator> atores = dto.atores().stream()
                .map(this::buscarOuCriarAtor)
                .collect(Collectors.toSet());

        filme.setAtores(atores);

        Filme salvo = filmeRepository.save(filme);
        return toResponseDTO(salvo);
    }

    public Page<FilmeResponseDTO> listar(String diretor, String nome, String genero, String ator, Pageable pageable) {
        return filmeRepository.buscarComFiltros(diretor, nome, genero, ator, pageable)
                .map(this::toResponseDTO);
    }

    public FilmeResponseDTO detalhar(Long id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado"));

        return toResponseDTO(filme);
    }

    private Ator buscarOuCriarAtor(String nome) {
        return atorRepository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> {
                    Ator novo = new Ator();
                    novo.setNome(nome);
                    return atorRepository.save(novo);
                });
    }

    private FilmeResponseDTO toResponseDTO(Filme filme) {
        List<String> nomesAtores = filme.getAtores().stream()
                .map(Ator::getNome)
                .collect(Collectors.toList());

        return new FilmeResponseDTO(
                filme.getId(),
                filme.getTitulo(),
                filme.getDiretor(),
                filme.getGenero(),
                nomesAtores,
                filme.getTotalVotos() == null ? 0L : filme.getTotalVotos(),
                filme.getMediaVotos() == null ? 0.0 : filme.getMediaVotos()
        );
    }
}
