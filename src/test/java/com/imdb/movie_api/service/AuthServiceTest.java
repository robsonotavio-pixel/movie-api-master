package com.imdb.movie_api.service;

import com.imdb.movie_api.dto.LoginRequestDTO;
import com.imdb.movie_api.dto.LoginResponseDTO;
import com.imdb.movie_api.entity.Administrador;
import com.imdb.movie_api.repository.AdministradorRepository;
import com.imdb.movie_api.repository.UsuarioRepository;
import com.imdb.movie_api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveLogarAdminComSucesso() {
        Administrador admin = new Administrador();
        admin.setEmail("admin@movieapi.com");
        admin.setSenha("hash");

        LoginRequestDTO request = new LoginRequestDTO("admin@movieapi.com", "admin123");

        when(administradorRepository.findByEmail(request.email())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(request.senha(), admin.getSenha())).thenReturn(true);
        when(jwtService.generateToken(admin.getEmail(), Map.of("role", "ROLE_ADMIN"))).thenReturn("token-fake");

        LoginResponseDTO resposta = authService.login(request);

        assertThat(resposta.token()).isEqualTo("token-fake");
        assertThat(resposta.tipo()).isEqualTo("Bearer");
    }

    @Test
    void deveLancarErroQuandoEmailNaoExiste() {
        LoginRequestDTO request = new LoginRequestDTO("naoexiste@teste.com", "123456");

        when(administradorRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void deveLancarErroQuandoSenhaErrada() {
        Administrador admin = new Administrador();
        admin.setEmail("admin@movieapi.com");
        admin.setSenha("hash");

        LoginRequestDTO request = new LoginRequestDTO("admin@movieapi.com", "senhaErrada");

        when(administradorRepository.findByEmail(request.email())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(request.senha(), admin.getSenha())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
