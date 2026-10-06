package com.imdb.movie_api.repository;

import com.imdb.movie_api.entity.Ator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtorRepository extends JpaRepository<Ator, Long> {
    Optional<Ator> findByNomeIgnoreCase(String nome);
}
