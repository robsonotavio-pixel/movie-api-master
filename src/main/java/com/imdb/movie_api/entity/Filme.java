package com.imdb.movie_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "filme")
@Getter
@Setter
public class Filme {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 200)
    private String titulo;

    @Column(nullable = false,length = 150)
    private String diretor;

    @Column(nullable = false,length = 100)
    private String genero;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Formula("(SELECT COUNT(*) FROM voto v WHERE v.filme_id = id)")
    private Long totalVotos;

    @Formula("(SELECT COALESCE(AVG(v.nota), 0) FROM voto v WHERE v.filme_id = id)")
    private Double mediaVotos;

    @ManyToMany
    @JoinTable(
          name = "filme_ator",
          joinColumns = @JoinColumn(name = "filme_id"),
          inverseJoinColumns = @JoinColumn(name = "ator_id")
    )
    private Set<Ator> atores = new HashSet<>();
}
