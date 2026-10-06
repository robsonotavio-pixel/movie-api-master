package com.imdb.movie_api.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ator")
@Getter
@Setter
public class Ator {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 150)
    private String nome;

    @ManyToMany(mappedBy = "atores")
    private Set<Filme> filmes = new HashSet<>();
}
