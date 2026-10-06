package com.imdb.movie_api.service;

import com.imdb.movie_api.dto.UsuarioCreateDTO;
import com.imdb.movie_api.dto.UsuarioResponseDTO;
import com.imdb.movie_api.entity.Usuario;
import com.imdb.movie_api.exception.ResourceNotFoundException;
import com.imdb.movie_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCriarUsuarioComSucesso() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Robson", "robson@teste.com", "123456");

        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.senha())).thenReturn("hash-fake");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponseDTO resultado = usuarioService.criar(dto);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Robson");
        assertThat(resultado.email()).isEqualTo("robson@teste.com");
    }

    @Test
    void deveLancarErroQuandoEmailJaCadastrado() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Robson", "robson@teste.com", "123456");

        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.of(new Usuario()));

        assertThatThrownBy(() -> usuarioService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email");
    }

    @Test
    void deveLancarErroAoEditarUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.editar(99L, new com.imdb.movie_api.dto.UsuarioUpdateDTO("X", "x@teste.com")))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
