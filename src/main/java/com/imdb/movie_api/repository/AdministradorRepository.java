package com.imdb.movie_api.repository;

import com.imdb.movie_api.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);
}
