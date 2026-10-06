package com.imdb.movie_api.entity;
 
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "voto", uniqueConstraints =  @UniqueConstraint(columnNames = {"filme_id", "usuario_id"}))
@Getter
@Setter
public class Voto {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "filme_id",nullable = false)
     private Filme filme;

     @ManyToOne
     @JoinColumn(name = "usuario_id", nullable = false)
     private Usuario usuario;

     @Column(nullable = false)
     private Integer nota;

     @Column(name = "created_at", nullable = false,updatable = false)
     private LocalDateTime createdAt = LocalDateTime.now();
}
