package com.imdb.movie_api.controller;

import com.imdb.movie_api.dto.UsuarioCreateDTO;
import com.imdb.movie_api.dto.UsuarioResponseDTO;
import com.imdb.movie_api.dto.UsuarioUpdateDTO;
import com.imdb.movie_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @PostMapping
    public UsuarioResponseDTO criar(@RequestBody @Valid UsuarioCreateDTO dto ) {
        return usuarioService.criar( dto);
    }
      @PutMapping("/{id}")
      public UsuarioResponseDTO editar(@PathVariable Long id,@RequestBody @Valid UsuarioUpdateDTO dto) {
           return usuarioService.editar(id, dto);
      }
      @DeleteMapping("/{id}")
      public void desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
      }
     
}
