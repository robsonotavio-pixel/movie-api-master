package com.imdb.movie_api.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "administrador")
@Getter
@Setter
public class Administrador {
    
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable = false,length = 150)
    private String nome;

    @Column(nullable = false,unique = true,length = 150)
    private String email;

    @Column( nullable = false,length = 255)
    private String senha;

    @Column(nullable = false)
     private Boolean ativo = true;

     @Column(name = "created_at",nullable = false,updatable = false) 
     private LocalDateTime createdAt = LocalDateTime.now();

}
