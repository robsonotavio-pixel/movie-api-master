package com.imdb.movie_api.service;

import com.imdb.movie_api.dto.VotoCreateDTO;
import com.imdb.movie_api.entity.Filme;
import com.imdb.movie_api.entity.Usuario;
import com.imdb.movie_api.entity.Voto;
import com.imdb.movie_api.exception.ResourceNotFoundException;
import com.imdb.movie_api.repository.FilmeRepository;
import com.imdb.movie_api.repository.UsuarioRepository;
import com.imdb.movie_api.repository.VotoRepository;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final FilmeRepository filmeRepository;
    private final UsuarioRepository usuarioRepository;

    public VotoService(
            VotoRepository votoRepository,
            FilmeRepository filmeRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.votoRepository = votoRepository;
        this.filmeRepository = filmeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void votar(Long filmeId, String emailUsuario, VotoCreateDTO dto) {
        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean jaVotou = votoRepository.findByFilmeIdAndUsuarioId(filmeId, usuario.getId()).isPresent();

        if (jaVotou) {
            throw new IllegalArgumentException("Usuário já votou nesse filme");
        }

        Voto voto = new Voto();
        voto.setFilme(filme);
        voto.setUsuario(usuario);
        voto.setNota(dto.nota());

        votoRepository.save(voto);
    }
}
