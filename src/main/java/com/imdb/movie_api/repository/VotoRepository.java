package com.imdb.movie_api.repository;

import com.imdb.movie_api.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByFilmeIdAndUsuarioId(Long filmeId, Long usuarioId);
    List<Voto> findByFilmeId(Long filmeId);
}
