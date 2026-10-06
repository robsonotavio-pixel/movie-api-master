package com.imdb.movie_api.service;

import com.imdb.movie_api.dto.AdministradorCreateDTO;
import com.imdb.movie_api.dto.AdministradorResponseDTO;
import com.imdb.movie_api.dto.AdministradorUpdateDTO;
import com.imdb.movie_api.dto.UsuarioResponseDTO;
import com.imdb.movie_api.exception.ResourceNotFoundException;
import com.imdb.movie_api.repository.UsuarioRepository;
import com.imdb.movie_api.entity.Administrador;
import com.imdb.movie_api.repository.AdministradorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
     private final AdministradorRepository administradorRepository;
     private final UsuarioRepository usuarioRepository;
     private final PasswordEncoder passwordEncoder;
  
     public AdminService (
        AdministradorRepository  administradorRepository,
        UsuarioRepository  usuarioRepository,
        PasswordEncoder passwordEncoder

     ){
        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
     }

     public AdministradorResponseDTO criar(AdministradorCreateDTO dto ) {
        if (administradorRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email ja cadrastrado");
     }

     Administrador admin = new Administrador();
     admin.setNome(dto.nome());
     admin.setEmail(dto.email());
     admin.setSenha(passwordEncoder.encode(dto.senha()));

      Administrador salvo = administradorRepository.save(admin);
      return toResponseDTO(salvo);
}
      public AdministradorResponseDTO editar(Long id, AdministradorUpdateDTO dto) {
        Administrador admin = administradorRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));

        admin.setNome(dto.nome());
        admin.setEmail(dto.email());

        Administrador salvo = administradorRepository.save(admin);
        return toResponseDTO(salvo);
      }

      public void desativar(Long id) {
        Administrador admin = administradorRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));

        admin.setAtivo(false);
        administradorRepository.save(admin);
      }

      public Page<UsuarioResponseDTO> listarUsuariosAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());

        return usuarioRepository.findByAtivoTrue(pageable)
                  .map(usuario -> new UsuarioResponseDTO(
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getAtivo()
                  ));
      }
      private AdministradorResponseDTO toResponseDTO(Administrador admin) {
        return new AdministradorResponseDTO(admin.getId(), admin.getNome(),admin.getEmail(),admin.getAtivo());
      }
      }
