package com.imdb.movie_api.repository;

import com.imdb.movie_api.entity.Filme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    @Query("""
            SELECT DISTINCT f FROM Filme f
            LEFT JOIN f.atores a
            WHERE f.ativo = true
            AND (:diretor IS NULL OR LOWER(f.diretor) LIKE LOWER(CONCAT('%', :diretor, '%')))
            AND (:nome IS NULL OR LOWER(f.titulo) LIKE LOWER(CONCAT('%', :nome, '%')))
            AND (:genero IS NULL OR LOWER(f.genero) LIKE LOWER(CONCAT('%', :genero, '%')))
            AND (:ator IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :ator, '%')))
            """)
    Page<Filme> buscarComFiltros(
            @Param("diretor") String diretor,
            @Param("nome") String nome,
            @Param("genero") String genero,
            @Param("ator") String ator,
            Pageable pageable
    );
}
