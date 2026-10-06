package com.imdb.movie_api.service;
   import com.imdb.movie_api.dto.LoginRequestDTO;
   import com.imdb.movie_api.dto.LoginResponseDTO;
   import com.imdb.movie_api.entity.Administrador;
   import com.imdb.movie_api.entity.Usuario;
   import com.imdb.movie_api.repository.AdministradorRepository;
   import com.imdb.movie_api.repository.UsuarioRepository;
   import com.imdb.movie_api.security.JwtService;
   import org.springframework.security.authentication.BadCredentialsException;
   import org.springframework.security.crypto.password.PasswordEncoder;
   import org.springframework.stereotype.Service;

   import java.util.Map;

   @Service
public class AuthService {
     private final AdministradorRepository administradorRepository;
     private final UsuarioRepository usuarioRepository;
     private final PasswordEncoder passwordEncoder;
     private final JwtService jwtService;

     public AuthService(
        AdministradorRepository administradorRepository,
        UsuarioRepository usuarioRepository,
        PasswordEncoder  passwordEncoder,
        JwtService  jwtService
     ){
        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
     }

     public LoginResponseDTO login(LoginRequestDTO request) {
        var adminOpt = administradorRepository.findByEmail(request.email());

        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
        if (passwordEncoder.matches(request.senha(), admin.getSenha())) {
            String token = jwtService.generateToken(admin.getEmail(), Map.of("role", "ROLE_ADMIN"));
            return new LoginResponseDTO(token, "Bearer");
        }
        throw new BadCredentialsException("Email ou senha invalidos");

        }
         var usuarioOpt = usuarioRepository.findByEmail(request.email());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(request.senha(), usuario.getSenha())) {
                String token = jwtService.generateToken(usuario.getEmail(), Map.of("role", "ROLE_USER"));
                return new LoginResponseDTO(token, "Bearer");
            }
        throw new BadCredentialsException("Email ou  senha invalidos");
        }
                throw new BadCredentialsException("Email ou senha inválidos");

     }
} 
